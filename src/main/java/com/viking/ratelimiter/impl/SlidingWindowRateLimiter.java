package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;

import java.time.Clock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowRateLimiter implements RateLimiter {
    //TODO: Remove stale entries
    private RateLimiterConfig rateLimiterConfig;
    private EventPublisher eventPublisher;
    private ConcurrentMap<Long, AtomicInteger> windows;

    public SlidingWindowRateLimiter(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher){
        this.eventPublisher = eventPublisher;
        this.rateLimiterConfig = rateLimiterConfig;
        this.windows = new ConcurrentHashMap<>();
    }
    @Override
    public boolean allow() {
        long currTime = Clock.systemUTC().millis();
        long currWindowKey = currTime/1000;
        long prevWindowKey = currWindowKey - 1;
        windows.putIfAbsent(currWindowKey, new AtomicInteger(0));
        AtomicInteger prevCnt = windows.get(prevWindowKey);
        if(prevCnt == null){
            if(windows.get(currWindowKey).incrementAndGet() <= rateLimiterConfig.getMaxRequests()){
                eventPublisher.onSuccess(currTime);
                return true;
            }else {
                eventPublisher.onFailure(currTime);
                return false;
            }
        }
        double prevWt = 1 - (currTime - (currWindowKey*1000))/1000;
        if(prevWt*prevCnt.get() + windows.get(currWindowKey).getAndIncrement() <= rateLimiterConfig.getMaxRequests()){
            eventPublisher.onSuccess(currTime);
            return true;
        }else{
            eventPublisher.onFailure(currTime);
            return false;
        }
    }
}
