package com.example.oversell.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("redisLock")
    public String redisLock() {
        log.info("enter the method");
        String key = "distributeTest";
        String value = UUID.randomUUID().toString();
        RedisCallback<Boolean> redisCallback = connection -> {
            // 缺失就设置
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.SET_IF_ABSENT;
            Expiration expiration = Expiration.seconds(60);
            byte[] keyByte = redisTemplate.getKeySerializer().serialize(key);
            byte[] valueByte = redisTemplate.getValueSerializer().serialize(value);

            Boolean result = connection.set(keyByte, valueByte, expiration, setOption);
            return result;
        };
        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);
        if (lock) {
            log.info("Enter the lock");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } finally {
                String script = "if redis.call(\"get\".KEYS[1]) == ARGV[1] then\n" +
                        "   return redis.call(\"del\", KEYS[1])\n" +
                        "else\n" +
                        "   return 0\n" +
                        "end";
                RedisScript<Boolean> redisScript = RedisScript.of(script);
                List<String> keys = Arrays.asList(key);
                Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
                log.info("result of unlock" + result);
            }
        }
        log.info("unlock");
        return "redisLock is finished";
    }
}
