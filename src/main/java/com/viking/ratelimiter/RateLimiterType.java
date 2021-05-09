package com.viking.ratelimiter;

import com.viking.visitors.RateLimiterVisitor;

public enum RateLimiterType {
    LEAKY_BUCKET{
        @Override
        public RateLimiter visit(RateLimiterVisitor visitor, RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
            return visitor.visitLeakyBucket(rateLimiterConfig, eventPublisher);
        }
    },
    TOKEN_BUCKET {
        @Override
        public RateLimiter visit(RateLimiterVisitor visitor, RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
            return visitor.visitLeakyBucket(rateLimiterConfig, eventPublisher);
        }
    },
    LAZY_TOKEN_BUCKET {
        @Override
        public RateLimiter visit(RateLimiterVisitor visitor, RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
            return visitor.visitLazyTokenBucket(rateLimiterConfig, eventPublisher);
        }
    },
    FIXED_WINDOW {
        @Override
        public RateLimiter visit(RateLimiterVisitor visitor, RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
            return visitor.visitFixedWindow(rateLimiterConfig, eventPublisher);
        }
    },
    SLIDING_WINDOW_LOG {
        @Override
        public RateLimiter visit(RateLimiterVisitor visitor, RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher) {
            return visitor.visitSlidingWindowLog(rateLimiterConfig, eventPublisher);
        }
    };

    public abstract RateLimiter visit(RateLimiterVisitor visitor, RateLimiterConfig rateLimiterConfig, EventPublisher eventPublisher);
}
