package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.services.ProductWatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductWatchingServiceImpl implements ProductWatchingService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PRODUCT_WATCHING_PREFIX = "watching_product_";
    @Override
    public void addWatcher(Long productId, String sessionId) {
        String key = PRODUCT_WATCHING_PREFIX + productId;
        redisTemplate.opsForSet().add(key, sessionId);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    @Override
    public int countWatchers(Long productId) {
        String key = PRODUCT_WATCHING_PREFIX + productId;
        Set<String> viewers = redisTemplate.opsForSet().members(key);
        return viewers != null ? viewers.size() : 0;
    }
}
