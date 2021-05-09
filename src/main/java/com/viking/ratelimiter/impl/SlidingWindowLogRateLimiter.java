package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowLogRateLimiter implements RateLimiter {
    private RateLimiterConfig rateLimiterConfig;
    private EventPublisher eventPublisher;
    private Queue<Long> log;

    public SlidingWindowLogRateLimiter(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher){
        this.eventPublisher = eventPublisher;
        this.rateLimiterConfig = rateLimiterConfig;
        this.log = new LinkedList<>();
    }

    @Override
    public boolean allow() {
        synchronized (log){
            long currTime = System.currentTimeMillis();
            long boundary = currTime - 1000;
            while (!log.isEmpty() && log.peek() <= boundary){
                log.poll();
            }
            log.add(currTime);
            if(log.size() <= rateLimiterConfig.getMaxRequests()){
                eventPublisher.onSuccess(currTime);
                return true;
            }else{
                eventPublisher.onFailure(currTime);
                return false;
            }
        }
    }
}
