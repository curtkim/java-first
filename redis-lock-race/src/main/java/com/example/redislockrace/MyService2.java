package com.example.redislockrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//@Service
public class MyService2 extends MyService{

    private static final Logger logger = LoggerFactory.getLogger(MyService2.class);

    LockDao lockDao(){
        return new LockDao(redisTemplate, "lock");
    }

    @Override
    void begin(String id, String value) {
        LockDao lockDao = lockDao();
        if(lockDao.lock(id)){
            beginInner(id, value);
            lockDao.release(id);
        }
    }
    void beginInner(String id, String value) {
        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                int newSum = Integer.parseInt(value);
                // save
                operations.multi();
                operations.opsForValue().set(statusKey(id), Status.READY.name());
                operations.opsForList().rightPush(listKey(id), value);
                operations.opsForValue().set(sumKey(id), newSum+"");
                return operations.exec();
            }
        });
        logger.info("{} {} {}", id, value, results);
    }

    @Override
    void append(String id, String value) {
        logger.info("({} {}", id, value);
        LockDao lockDao = lockDao();
        if(lockDao.lock(id)){
           appendInner(id, value);
           lockDao.release(id);
        }
    }
    List<Object> appendInner(String id, String value){
        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                // 1.watch
                operations.watch(sumKey(id));

                String strStatus = (String)operations.opsForValue().get(statusKey(id));
                Status oldStatus = Status.valueOf(strStatus);
                if(oldStatus != Status.READY)
                    return Arrays.asList();

                operations.opsForValue().set(statusKey(id), Status.APPENDING.name());
                //redisTemplate.watch(verKey(id));

                // 2.get
                String strSum = (String)operations.opsForValue().get(sumKey(id));
                if(strSum == null)
                    return Arrays.asList();

                // 3.calculate
                int newSum = Integer.parseInt(strSum)+ Integer.parseInt(value);
                sleepUnchecked(generateRandom(100));

                // 4.save
                operations.multi();
                operations.opsForValue().increment(verKey(id));
                operations.opsForValue().set(statusKey(id), Status.READY.name());

                operations.opsForList().rightPush(listKey(id), value);
                operations.opsForValue().set(sumKey(id), newSum+"");
                return operations.exec();
            }
        });
        // 성공시 : [list_length, set_success?]
        // 실패시 : []
        logger.info("){} {} {}", id, value, results);
        return results;
    }

    @Override
    void end(String id, String value) {
        logger.info("({} {}", id, value);
        endInner(id, value);
    }

    List<Object> endInner(String id, String value) {
        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                // 1.watch
                operations.opsForValue().increment(verKey(id));
                operations.opsForValue().set(statusKey(id), Status.ENDING.name());

                // 2.get
                String strSum = (String)operations.opsForValue().get(sumKey(id));
                if(strSum == null)
                    return Arrays.asList();

                // 3.calculate
                int newSum = Integer.parseInt(strSum)+ Integer.parseInt(value);
                sleepUnchecked(generateRandom(100));

                // 4.save
                operations.multi();
                operations.opsForValue().set(statusKey(id), Status.COMPLETE.name());

                operations.opsForList().rightPush(listKey(id), value);
                operations.opsForValue().set(sumKey(id), newSum+"");
                return operations.exec();
            }
        });
        // 성공시 : [list_length, set_success?]
        // 실패시 : []

        logger.info("){} {} {}", id, value, results);
        return results;

    }
}
