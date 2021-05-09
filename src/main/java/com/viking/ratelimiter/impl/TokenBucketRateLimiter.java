package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

public class TokenBucketRateLimiter implements RateLimiter {
    private RateLimiterConfig rateLimiterConfig;
    private Integer tokens;
    private EventPublisher eventPublisher;

    public TokenBucketRateLimiter(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher){
        this.eventPublisher = eventPublisher;
        new Thread(() -> {
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this){
                    tokens = rateLimiterConfig.getMaxRequests();
                    notifyAll();
                }
            }

        }).start();
    }

    @Override
    public boolean allow() {
        synchronized (this){
            if(tokens == 0){
                eventPublisher.onFailure(Clock.systemUTC().millis());
                return false;
            }else{
                tokens--;
                eventPublisher.onSuccess(Clock.systemUTC().millis());
                return true;
            }
        }
    }
}
