package com.example.lettuce.data;

import io.lettuce.core.api.StatefulConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

@Slf4j
public class LettuceConnectionUtils {

  public static StatefulConnection<String, String> getConnection(GenericObjectPool<StatefulConnection<String, String>> pool) {
    return doGetConnection(pool, true);
  }

  static StatefulConnection<String, String> fetchConnection(GenericObjectPool<StatefulConnection<String, String>> pool){
    try {
      return pool.borrowObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static StatefulConnection<String, String> doGetConnection(GenericObjectPool<StatefulConnection<String, String>> pool, boolean allowCreate) {

    Assert.notNull(pool, "No RedisConnectionFactory specified");

    LettuceConnectionHolder conHolder = (LettuceConnectionHolder) TransactionSynchronizationManager.getResource(pool);

    if (conHolder != null && (conHolder.hasConnection() || conHolder.isSynchronizedWithTransaction())) {
      conHolder.requested();
      if (!conHolder.hasConnection()) {
        log.debug("Fetching resumed Redis Connection from RedisConnectionFactory");
        conHolder.setConnection(fetchConnection(pool));
      }
      return conHolder.getRequiredConnection();
    }

    // Else we either got no holder or an empty thread-bound holder here.

    if (!allowCreate) {
      throw new IllegalArgumentException("No connection found and allowCreate = false");
    }

    log.debug("Fetching Redis Connection from RedisConnectionFactory");
    StatefulConnection<String, String> connection = fetchConnection(pool);

    try {
      // Use same RedisConnection for further Redis actions within the transaction.
      // Thread-bound object will get removed by synchronization at transaction completion.
      LettuceConnectionHolder holderToUse = conHolder;
      if (holderToUse == null) {
        holderToUse = new LettuceConnectionHolder(connection);
      } else {
        holderToUse.setConnection(connection);
      }
      holderToUse.requested();

      if (holderToUse != conHolder) {
        TransactionSynchronizationManager.bindResource(pool, holderToUse);
      }
    } catch (RuntimeException ex) {
      // Unexpected exception from external delegation call -> close Connection and rethrow.
      releaseConnection(connection, pool);
      throw ex;
    }
    return connection;
  }

  public static void releaseConnection(@Nullable StatefulConnection<String, String> conn, GenericObjectPool<StatefulConnection<String, String>> pool) {
    if (conn == null) {
      return;
    }

    LettuceConnectionHolder conHolder = (LettuceConnectionHolder) TransactionSynchronizationManager.getResource(pool);
    if (conHolder != null) {
      // release transactional/read-only and non-transactional/non-bound connections.
      // transactional connections for read-only transactions get no synchronizer registered

      unbindConnection(pool);
      return;
    }

    doCloseConnection(conn);
  }

  public static void unbindConnection(GenericObjectPool<StatefulConnection<String, String>> pool) {

    LettuceConnectionHolder conHolder = (LettuceConnectionHolder) TransactionSynchronizationManager.getResource(pool);

    if (conHolder == null) {
      return;
    }

    if (log.isDebugEnabled()) {
      log.debug("Unbinding Redis Connection.");
    }

    StatefulConnection<String, String> connection = conHolder.getConnection();
    conHolder.released();

    if (!conHolder.isOpen()) {
      TransactionSynchronizationManager.unbindResourceIfPossible(pool);
      doCloseConnection(connection);
    }
  }

  private static void doCloseConnection(@Nullable StatefulConnection<String, String> connection) {

    if (connection == null) {
      return;
    }

    if (log.isDebugEnabled()) {
      log.debug("Closing Redis Connection.");
    }

    try {
      connection.close();
    } catch (DataAccessException ex) {
      log.debug("Could not close Redis Connection", ex);
    } catch (Throwable ex) {
      log.debug("Unexpected exception on closing Redis Connection", ex);
    }
  }
}
