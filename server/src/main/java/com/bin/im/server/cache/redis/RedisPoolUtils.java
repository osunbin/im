package com.bin.im.server.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class RedisPoolUtils {
    private static JedisPool jedisPool = null;

    /**
     * redis服务器地址
     */
    private static String addr = "127.0.0.1";

    /**
     * redis服务器端口
     */
    private static int port = 6379;

    /**
     * redis服务器密码
     */
    private static String auth = "111111";

    //可用连接实例的最大数目，默认为8；
    //如果赋值为-1，则表示不限制，如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
    private static Integer MAX_TOTAL = 1024;
    //控制一个pool最多有多少个状态为idle(空闲)的jedis实例，默认值是8
    private static Integer MAX_IDLE = 200;
    //等待可用连接的最大时间，单位是毫秒，默认值为-1，表示永不超时。
    //如果超过等待时间，则直接抛出JedisConnectionException
    private static Integer MAX_WAIT_MILLIS = 10000;
    //客户端超时时间配置
    private static Integer TIMEOUT = 10000;
    //在borrow(用)一个jedis实例时，是否提前进行validate(验证)操作；
    //如果为true，则得到的jedis实例均是可用的
    private static Boolean TEST_ON_BORROW = true;
    //在空闲时检查有效性, 默认false
    private static Boolean TEST_WHILE_IDLE = true;
    //是否进行有效性检查
    private static Boolean TEST_ON_RETURN = true;


    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();

            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWait(Duration.ofMillis(MAX_WAIT_MILLIS));
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestWhileIdle(TEST_WHILE_IDLE);
            config.setTestOnReturn(TEST_ON_RETURN);
            String masterName = "mymaster";

            // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
            config.setBlockWhenExhausted(true);
            // 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
            config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
            // 是否启用pool的jmx管理功能, 默认true
            config.setJmxEnabled(true);
            // MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
            config.setJmxNamePrefix("pool");
            // 是否启用后进先出, 默认true
            config.setLifo(true);
            // 最大空闲连接数, 默认8个
            config.setMaxIdle(8);
            // 最大连接数, 默认8个
            config.setMaxTotal(8);
            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
            config.setMaxWait(Duration.ofMillis(-1));
            // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
            config.setMinEvictableIdleTimeMillis(1800000);
            // 最小空闲连接数, 默认0
            config.setMinIdle(0);
            // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
            config.setNumTestsPerEvictionRun(3);
            // 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
            config.setSoftMinEvictableIdleTimeMillis(1800000);
            // 在获取连接的时候检查有效性, 默认false
            config.setTestOnBorrow(false);
            // 在空闲时检查有效性, 默认false
            config.setTestWhileIdle(false);
            // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
            config.setTimeBetweenEvictionRuns(Duration.ofMillis(-1));
            jedisPool = new JedisPool(config, addr, port, TIMEOUT, auth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Jedis 资源
     *
     * @return
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 释放Jedis资源
     */
    public static void close(final Jedis jedis) {

        jedis.close();

    }
}
