package org.example.musk.plugin.lock.core.impl;


import jakarta.annotation.Resource;
import org.example.musk.middleware.redis.RedisUtil;
import org.example.musk.plugin.lock.core.Lock;

public class RedisLock implements Lock {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean tryLock(String key, long ttl) {
        return redisUtil.tryLock(key, ttl);
    }

    @Override
    public boolean unLock(String key) {
        return redisUtil.unLock(key);
    }
}
