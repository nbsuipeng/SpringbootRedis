package com.sp.redis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate=null;
    @Autowired
    private StringRedisTemplate stringRedisTemplate=null;

    @RequestMapping("/stringAndHash")
    @ResponseBody
    public Map<String,Object> testStringAndHash()
    {
        redisTemplate.opsForValue().set("key1","value1");
        //注意这里使用了JDK的序列化器，所以Redis保存时不是整数，不能运算
        redisTemplate.opsForValue().set("int_key","1");
        stringRedisTemplate.opsForValue().set("int","1");
        //获取底层Redis连接
        Jedis jedis = (Jedis) stringRedisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        jedis.decr("int");
        String s= (String) redisTemplate.opsForValue().get("int_key");
        Map<String,String> hash=new HashMap<>();
        hash.put("fields1","value1");
        hash.put("fields2","value2");
        //存入一个散列数据类型
        stringRedisTemplate.opsForHash().putAll("hash",hash);
        //新增一个字段
        stringRedisTemplate.opsForHash().put("hash","field3","value3");
        //绑定散列操作的key，这样可以连续对同一个散列数据类型进行操作
        BoundHashOperations hashOps=stringRedisTemplate.boundHashOps("hash");
        //删除两个字段
        hashOps.delete("field1","field2");
        //新增一个字段
        hashOps.put("field4","value4");
        Map<String,Object> map=new HashMap<>();
        map.put("success",true);
        map.put("int_key",s);
        return map;
    }
}
