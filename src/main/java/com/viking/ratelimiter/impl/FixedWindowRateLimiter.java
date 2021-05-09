package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.viking.constants.Constants.MILLISECONDS;

public class FixedWindowRateLimiter implements RateLimiter {
    private RateLimiterConfig rateLimiterConfig;
    private EventPublisher eventPublisher;
    private ConcurrentMap<Long, AtomicInteger> windows;

    public FixedWindowRateLimiter(RateLimiterConfig rateLimiterConfig,
                                  EventPublisher eventPublisher){
        this.rateLimiterConfig = rateLimiterConfig;
        this.eventPublisher = eventPublisher;
        this.windows = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allow() {
        long currTime = System.currentTimeMillis();
        long windowKey = (currTime/MILLISECONDS);
        windows.putIfAbsent(windowKey, new AtomicInteger(0));
        if(windows.get(windowKey).incrementAndGet() <= rateLimiterConfig.getMaxRequests()){
            eventPublisher.onSuccess(System.currentTimeMillis());
            return true;
        }else {
            eventPublisher.onFailure(System.currentTimeMillis());
            return false;
        }
    }
}
