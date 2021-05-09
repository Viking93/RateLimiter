package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;
import com.viking.utilities.TimeUtils;
import lombok.Data;

import java.time.Clock;

@Data
public class LeakyBucketRateLimiter implements RateLimiter {
    private RateLimiterConfig rateLimiterConfig;
    private Integer REQUEST_INTERVAL_TIME;
    private Long nextAllowedTime;
    private Clock clock;
    private EventPublisher eventPublisher;

    public LeakyBucketRateLimiter(RateLimiterConfig rateLimiterConfig, Clock clock) {
        this.rateLimiterConfig = rateLimiterConfig;
        this.REQUEST_INTERVAL_TIME = TimeUtils.getTimeRequestInterval(rateLimiterConfig.getDuration()) / rateLimiterConfig.getMaxRequests();
        this.clock = clock;
        this.nextAllowedTime = clock.millis();
        this.eventPublisher = new ConsoleEventPublisher();
    }

    public LeakyBucketRateLimiter(RateLimiterConfig rateLimiterConfig) {
        this.rateLimiterConfig = rateLimiterConfig;
        this.REQUEST_INTERVAL_TIME = TimeUtils.getTimeRequestInterval(rateLimiterConfig.getDuration()) / rateLimiterConfig.getMaxRequests();
        this.clock = Clock.systemUTC();
        this.nextAllowedTime = clock.millis();
        this.eventPublisher = new ConsoleEventPublisher();
    }

    public LeakyBucketRateLimiter(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
        this.rateLimiterConfig = rateLimiterConfig;
        this.REQUEST_INTERVAL_TIME = TimeUtils.getTimeRequestInterval(rateLimiterConfig.getDuration()) / rateLimiterConfig.getMaxRequests();
        this.clock = Clock.systemUTC();
        this.nextAllowedTime = clock.millis();
        this.eventPublisher = eventPublisher;
    }


    @Override
    public boolean allow() {
        Long currTime = clock.millis();
        synchronized (this) {
            if(currTime >= nextAllowedTime){
                nextAllowedTime = currTime + REQUEST_INTERVAL_TIME;
                eventPublisher.onSuccess(currTime);
                return true;
            }else{
                eventPublisher.onFailure(currTime);
                return false;
            }
        }
    }
}
