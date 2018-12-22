package com.tipslee.distributed.intercept.aspect;

import com.tipslee.distributed.intercept.AbstractLockAspect;
import com.tipslee.distributed.intercept.IdGenerator;
import com.tipslee.distributed.jedis.JedisCluster;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import redis.clients.jedis.Jedis;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class RedisLockAspect extends AbstractLockAspect {

    private int lockTimeOutSeconds; //锁过期时间
    private IdGenerator idGenerator; //ID标识生成器
    private JedisCluster jedisCluster; //redis客户端

    @Override
    public void lock(ProceedingJoinPoint jp) throws Throwable {
        String lockKey = getJoinPointName(jp);
        //获取的当前docker唯一标示码
        String uniqueId = idGenerator.getUniqueId();
        Jedis client = jedisCluster.getClient();
        long result = client.setnx(lockKey, uniqueId);
        if (result >= 1) {
            client.expire(lockKey, lockTimeOutSeconds);
            jp.proceed();
            unlock(jp, null);
        } else {
            //当执行过程中，docker重启发布，启动后还可以继续执行
            String uniqueValue = client.get(lockKey);
            if (uniqueId.equals(uniqueValue)) {
                jp.proceed();
                unlock(jp, null);
            }
        }
    }

    @Override
    public void unlock(JoinPoint jp, Throwable e) {
        Jedis client = null;
        try {
            String lockKey = getJoinPointName(jp);
            client = jedisCluster.getClient();
            client.del(lockKey);
        } finally {
            client.close();
        }
    }

    public void setLockTimeOutSeconds(int lockTimeOutSeconds) {
        this.lockTimeOutSeconds = lockTimeOutSeconds;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
}
