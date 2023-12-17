package com.campusconnect.email.otp.redis;

import com.campusconnect.email.otp.OTPStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Primary
public class RedisOTPStrategy implements OTPStrategy {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisProperties properties;

    public RedisOTPStrategy(RedisTemplate<String, String> redisTemplate, RedisProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    @Override
    public String setOTPForEmail(String email) {
        String otp = OTPStrategy.generateRandomString(6).toUpperCase();
        log.info("setting otp {} for email {}", otp, email);
        redisTemplate.opsForValue().set(email, otp, properties.getTtl(), TimeUnit.SECONDS);
        return otp;
    }

    @Override
    public String getOTPForEmail(String email) {
        String otp = redisTemplate.opsForValue().get(email);
        log.info("otp for email {} was {}", email, otp);
        return otp;
    }
}
