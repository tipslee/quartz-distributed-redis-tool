package com.tipslee.distributed.jedis;

import redis.clients.jedis.Jedis;

/**
 * @author liqiang
 * @description
 * @time 2018年12月21日
 * @modifytime
 */
public class JedisCluster {

    public Jedis getClient() {
        return JedisPoolManager.getInstance().getResource();
    }
}
