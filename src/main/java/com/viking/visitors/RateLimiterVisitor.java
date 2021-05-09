package com.viking.visitors;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;

public interface RateLimiterVisitor {
    RateLimiter visitLeakyBucket(final RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher);
    RateLimiter visitTokenBucket(final RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher);
    RateLimiter visitLazyTokenBucket(final RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher);
    RateLimiter visitFixedWindow(final RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher);
    RateLimiter visitSlidingWindowLog(final RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher);
}
