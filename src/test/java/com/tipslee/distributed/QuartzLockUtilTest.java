package com.tipslee.distributed;

import com.tipslee.distributed.constant.RedisToolsConstant;
import com.tipslee.distributed.intercept.lock.RedisLock;
import com.tipslee.distributed.intercept.uid.UUIDGenerator;
import com.tipslee.distributed.quartz.AbstractQuartz;
import com.tipslee.distributed.quartz.ISingleRunnableWorker;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class QuartzLockUtilTest {


    private RedisLock redisLock;

    @Mock
    private JedisConnectionFactory jedisConnectionFactory ;

    @Mock
    private JedisCluster jedisCluster;

    @Before
    public void setBefore() {
        MockitoAnnotations.initMocks(this);

        redisLock = new RedisLock.Builder(jedisConnectionFactory, RedisToolsConstant.CLUSTER, new UUIDGenerator(), 60)
                .setLockPrefix("lock_")
                .build();
        RedisClusterConnection clusterConnection = new JedisClusterConnection(jedisCluster);
        Mockito.when(jedisConnectionFactory.getClusterConnection()).thenReturn(clusterConnection);
        jedisCluster = (JedisCluster)clusterConnection.getNativeConnection();
    }

    @Test
    public void quartz2Run() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-config.xml");//初始化spring 容器});
        AbstractQuartz worker = (AbstractQuartz) appContext.getBean("syncInfoQuartz2");
        Mockito.when(jedisCluster.set(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyLong())).thenReturn("OK");
        Mockito.when(jedisCluster.del(Mockito.anyString())).thenReturn(1L);

        String key = worker.getClazzAndMethodName(worker);
        if (redisLock.tryLock(key)) {
            worker.doWork();
            redisLock.unLcok(key);
        }
    }



}
