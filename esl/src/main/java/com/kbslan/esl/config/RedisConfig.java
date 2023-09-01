package com.kbslan.esl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig {

	@Bean(name = "jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig(@Value("${spring.redis.maxTotal}") int maxTotal,
                                           @Value("${spring.redis.maxTotal}") int maxIdle,
                                           @Value("${spring.redis.maxWaitMillis}") long maxWaitMillis,
                                           @Value("${spring.redis.testOnBorrow}") boolean testOnBorrow) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    @Bean(name = "jedisPool")
    public JedisPool jedisPool(@Autowired JedisPoolConfig jedisPoolConfig,
                               @Value("${spring.redis.host}") String host,
                               @Value("${spring.redis.password}") String password,
                               @Value("${spring.redis.port}") Integer port) {
        return new JedisPool(jedisPoolConfig, host, port, 2000, "".equals(password) ? null : password);
    }


    @Bean(name = "redisUtils")
    public RedisUtils redisUtils(@Autowired JedisPool jedisPool, @Value("${spring.redis.expireTime}") int expireTime) {
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.setJedisPool(jedisPool);
        redisUtils.setExpireTime(expireTime);
        return redisUtils;
    }

}
