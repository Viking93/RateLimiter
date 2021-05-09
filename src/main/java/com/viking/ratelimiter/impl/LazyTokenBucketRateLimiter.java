package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class LazyTokenBucketRateLimiter implements RateLimiter {
    private RateLimiterConfig rateLimiterConfig;
    private Integer tokens;
    private EventPublisher eventPublisher;
    private Long lastRefillTime;

    public LazyTokenBucketRateLimiter(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher){
        this.rateLimiterConfig = rateLimiterConfig;
        this.eventPublisher = eventPublisher;
        this.lastRefillTime = System.currentTimeMillis();
        this.tokens = rateLimiterConfig.getMaxRequests();
    }

    @Override
    public boolean allow() {
        synchronized (this){
            refillTokens();
            if(tokens == 0){
                eventPublisher.onFailure(System.currentTimeMillis());
                return false;
            }else {
                eventPublisher.onSuccess(System.currentTimeMillis());
                tokens--;
                return true;
            }
        }
    }

    private void refillTokens(){
        Long currTime = System.currentTimeMillis();
        int secSinceLastRefill = (int)(currTime - lastRefillTime)/1000;
        int cnt = secSinceLastRefill * rateLimiterConfig.getMaxRequests();
        if(cnt > 0){
            tokens = Math.min(tokens + cnt, rateLimiterConfig.getMaxRequests());
            lastRefillTime = currTime;
        }
    }
}
