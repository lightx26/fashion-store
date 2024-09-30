package com.pbl.fashionstore.services;

public interface ProductWatchingService {
    void addWatcher(Long productId, String sessionId);
    int countWatchers(Long productId);
}
