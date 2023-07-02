package com.example.lettuce.data;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.ResourceHolderSupport;


class LettuceConnectionHolder extends ResourceHolderSupport {

  @Nullable
  private StatefulConnection<String, String> connection;

  private boolean transactionActive = false;

  public LettuceConnectionHolder(StatefulConnection<String, String> connection) {
    this.connection = connection;
  }

  protected boolean hasConnection() {
    return (this.connection != null);
  }

  @Nullable
  public StatefulConnection<String, String> getConnection() {
    return connection;
  }

  public StatefulConnection<String, String> getRequiredConnection() {

    StatefulConnection<String, String> connection = getConnection();

    if (connection == null) {
      throw new IllegalStateException("No active RedisConnection");
    }

    return connection;
  }

  protected void setConnection(@Nullable StatefulConnection<String, String> connection) {
    this.connection = connection;
  }

  /**
   * Releases the current Connection held by this ConnectionHolder.
   * <p>
   * This is necessary for ConnectionHandles that expect "Connection borrowing", where each returned Connection is
   * only temporarily leased and needs to be returned once the data operation is done, to make the Connection
   * available for other operations within the same transaction.
   */
  @Override
  public void released() {
    super.released();
    if (!isOpen()) {
      setConnection(null);
    }
  }

  @Override
  public void clear() {
    super.clear();
    this.transactionActive = false;
  }
}
