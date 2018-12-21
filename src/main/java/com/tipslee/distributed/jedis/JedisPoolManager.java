package com.tipslee.distributed.jedis;

import com.tipslee.distributed.util.PropertyUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class JedisPoolManager {
    private static volatile JedisPoolManager instance;
    private final JedisPool pool;

    public static JedisPoolManager getInstance() {
        if (instance == null) {
            synchronized (JedisPoolManager.class) {
                if (instance == null) {
                    instance = new JedisPoolManager();
                }
            }
        }
        return instance;
    }

    public Jedis getResource() {
        return pool.getResource();
    }

    public void destroy() {
        // when closing your application:
        pool.destroy();
    }

    public void close() {
        pool.close();
    }

    public JedisPoolManager() {
        try {
            //加载redis配置
            Properties props = PropertyUtils.load("/redis.properties");

            // 创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();

            // 设置池配置项值
            String maxTotal = props.getProperty("redis.pool.maxTotal", "4");
            config.setMaxTotal(Integer.parseInt(maxTotal));

            String maxIdle = props.getProperty("redis.pool.maxIdle", "4");
            config.setMaxIdle(Integer.parseInt(maxIdle));

            String minIdle = props.getProperty("redis.pool.minIdle", "1");
            config.setMinIdle(Integer.parseInt(minIdle));

            String maxWaitMillis = props.getProperty("redis.pool.maxWaitMillis", "1024");
            config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));

            String testOnBorrow = props.getProperty("redis.pool.testOnBorrow", "true");
            config.setTestOnBorrow("true".equals(testOnBorrow));

            String testOnReturn = props.getProperty("redis.pool.testOnReturn", "true");
            config.setTestOnReturn("true".equals(testOnReturn));

            String server = props.getProperty("redis.server");
            if (StringUtils.isEmpty(server)) {
                throw new IllegalArgumentException("JedisPool redis.server is empty!");
            }

            String[] host_arr = server.split(",");
            if (host_arr.length > 1) {
                throw new IllegalArgumentException("JedisPool redis.server length > 1");
            }

            String[] arr = host_arr[0].split(":");

            // 根据配置实例化jedis池
            System.out.println("***********init JedisPool***********");
            System.out.println("host->" + arr[0] + ",port->" + arr[1]);

            pool = new JedisPool(config, arr[0], Integer.parseInt(arr[1]));

        } catch (Exception e) {
            throw new IllegalArgumentException("init JedisPool error", e);
        }
    };
}
