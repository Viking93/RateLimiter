package com.viking.visitors.impl;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;
import com.viking.ratelimiter.impl.*;
import com.viking.visitors.RateLimiterVisitor;

public class RateLimiterVisitorImpl implements RateLimiterVisitor {
    @Override
    public RateLimiter visitLeakyBucket(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
        return new LeakyBucketRateLimiter(rateLimiterConfig, eventPublisher);
    }

    @Override
    public RateLimiter visitTokenBucket(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
        return new TokenBucketRateLimiter(rateLimiterConfig, eventPublisher);
    }

    @Override
    public RateLimiter visitLazyTokenBucket(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
        return new LazyTokenBucketRateLimiter(rateLimiterConfig, eventPublisher);
    }

    @Override
    public RateLimiter visitFixedWindow(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
        return new FixedWindowRateLimiter(rateLimiterConfig, eventPublisher);
    }

    @Override
    public RateLimiter visitSlidingWindowLog(RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
        return new SlidingWindowLogRateLimiter(rateLimiterConfig, eventPublisher);
    }
}
