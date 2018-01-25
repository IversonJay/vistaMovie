package com.lhh.vista.common.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by lifx on 2016/5/24.
 */
public class JedisPoolUtil {

    private JedisPool jedisPool;

    public JedisPoolUtil(String ip, int port, String password) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxTotal(-1);
        config.setMaxWaitMillis(60 * 1000);
        jedisPool = new JedisPool(config, ip, port, 5000, password);
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void backJedis(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    public final static String MARK_SERVER_STATE = "SYS_SERVER_STATE_";

    public final static String MARK_CLIENT_LIST = "SYS_CLIENT_LIST_";
}
