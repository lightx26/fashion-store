package com.pbl.fashionstore.services.impl;

import com.pbl.fashionstore.services.ProductWatchingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductWatchingServiceImpl implements ProductWatchingService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PRODUCT_WATCHING_PREFIX = "watching_product_";
    private static final Logger logger = LoggerFactory.getLogger(ProductWatchingServiceImpl.class);

    @Override
    public void addWatcher(Long productId, String sessionId) {
        String key = PRODUCT_WATCHING_PREFIX + productId;
        try {
            redisTemplate.opsForSet().add(key, sessionId);
            redisTemplate.expire(key, 10, TimeUnit.MINUTES);
        } catch (RedisConnectionFailureException e) {
            logger.error("Failed to connect to Redis: {}", e.getMessage());
        }
    }

    @Override
    public int countWatchers(Long productId) {
        String key = PRODUCT_WATCHING_PREFIX + productId;
        try {
            Set<String> viewers = redisTemplate.opsForSet().members(key);
            return viewers != null ? viewers.size() : 0;
        } catch (RedisConnectionFailureException e) {
            logger.error("Failed to connect to Redis: {}", e.getMessage());
            return 1;
        }
    }
}
