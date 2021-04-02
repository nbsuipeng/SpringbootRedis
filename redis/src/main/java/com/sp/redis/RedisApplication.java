package com.sp.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = ("com.sp.redis"))
public class RedisApplication {
    @Autowired
    //RedisTemplate会使用JdkSerializationRedisSerializer序列化键值
    private RedisTemplate redisTemplate=null;
    //定义自定义后初始化方法
    @PostConstruct
    public void init()
    {
        initRedisTemplate();
    }
//    设置RedisTemplate的序列化器
    private void initRedisTemplate() {
        //下面是把Redis序列化键值的类改为stringSerializer
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

    }

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
