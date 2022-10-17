package com.atguigu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 包名:com.atguigu
 *
 * @author Leevi
 * 日期2022-10-13  14:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/spring-redis.xml")
public class JedisTest {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void test01(){
        Jedis jedis = jedisPool.getResource();
        String address = jedis.get("address");
        System.out.println(address);
    }
}
