package com.tipslee.distributed.intercept.lock;

import com.tipslee.distributed.constant.RedisToolsConstant;
import com.tipslee.distributed.intercept.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * @author liqiang
 * @description
 * @time 2018年12月22日
 * @modifytime
 */
public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private static final String LOCK_MSG = "OK";

    private static final Long UNLOCK_MSG = 1L;

    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";

    private String lockPrefix;
    private int lockTimeOutSeconds;
    private int type;
    private JedisConnectionFactory jedisConnectionFactory;
    private IdGenerator idGenerator;


    public RedisLock(Builder builder) {
        this.jedisConnectionFactory = builder.jedisConnectionFactory;
        this.lockPrefix = builder.lockPrefix;
        this.lockTimeOutSeconds = builder.lockTimeOutSeconds;
        this.idGenerator = builder.idGenerator;
        this.type = builder.type;
    }

    public boolean tryLock(String key) {
        Object connection = getConnection();
        String result = null;
        String uniqueId = idGenerator.getUniqueId();
        if (connection instanceof Jedis) {
            result = ((Jedis) connection).set(lockPrefix + key, uniqueId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, lockTimeOutSeconds);
        } else {
            result = ((JedisCluster) connection).set(lockPrefix + key, uniqueId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, lockTimeOutSeconds);
        }
        if (LOCK_MSG.equals(result)) {
            return true;
        } else {
            String uniqueResult = null;
            if (connection instanceof Jedis) {
                uniqueResult = ((Jedis) connection).get(lockPrefix + key);
            } else {
                uniqueResult = ((JedisCluster) connection).get(lockPrefix + key);
            }
            if (uniqueId.equals(uniqueResult)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean unLcok(String key) {
        Object connection = getConnection();
        Long result = null;
        if (connection instanceof Jedis) {
            result = ((Jedis) connection).del(lockPrefix + key);
        } else {
            result = ((JedisCluster) connection).del(lockPrefix + key);
        }
        if (UNLOCK_MSG.equals(result)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get Redis connection
     * @return
     */
    private Object getConnection() {
        Object connection ;
        if (type == RedisToolsConstant.SINGLE){
            RedisConnection redisConnection = jedisConnectionFactory.getConnection();
            connection = redisConnection.getNativeConnection();
        }else {
            RedisClusterConnection clusterConnection = jedisConnectionFactory.getClusterConnection();
            connection = clusterConnection.getNativeConnection() ;
        }
        return connection;
    }

    public static class Builder {
        private JedisConnectionFactory jedisConnectionFactory = null ;
        private String lockPrefix = "lock_";
        private int type;
        private IdGenerator idGenerator;
        private int lockTimeOutSeconds;

        public Builder(JedisConnectionFactory jedisConnectionFactory, int type, IdGenerator idGenerator, int lockTimeOutSeconds) {
            this.jedisConnectionFactory = jedisConnectionFactory;
            this.type = type;
            this.idGenerator = idGenerator;
            this.lockTimeOutSeconds = lockTimeOutSeconds;
        }

        public Builder setLockPrefix(String prefix) {
            this.lockPrefix = prefix;
            return this;
        }

        public RedisLock build() {
            return new RedisLock(this);
        }
    }

}
