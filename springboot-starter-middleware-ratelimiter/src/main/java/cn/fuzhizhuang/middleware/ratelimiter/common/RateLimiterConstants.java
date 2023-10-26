package cn.fuzhizhuang.middleware.ratelimiter.common;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fuzhizhuang
 * @description RateLimiter限流常量
 */
public class RateLimiterConstants {
    public static Map<String, RateLimiter> rateLimiterMap = Collections.synchronizedMap(new HashMap<String, RateLimiter>());
}
