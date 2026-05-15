package com.example.toeicwebsite.infrastucture.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedisAspect {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<>();

    @Around("@annotation(redisCache)")
    public Object process(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
        String parsedKey;
        try {
            parsedKey = parse(joinPoint, redisCache.key());
        } catch (Exception e) {
            log.warn("[RedisCache] Failed to parse key expression '{}', skipping cache: {}", redisCache.key(), e.getMessage());
            return joinPoint.proceed();
        }

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        JavaType returnType = objectMapper.getTypeFactory().constructType(method.getGenericReturnType());

        // Cache hit — first check (outside lock)
        try {
            String json = redisTemplate.opsForValue().get(parsedKey);
            if (json != null) {
                return objectMapper.readValue(json, returnType);
            }
        } catch (Exception e) {
            log.warn("[RedisCache] Cache read failed for key '{}', falling through to DB: {}", parsedKey, e.getMessage());
            return joinPoint.proceed();
        }

        // Cache miss — acquire lock and double-check
        Object lock = lockMap.computeIfAbsent(parsedKey, k -> new Object());
        synchronized (lock) {
            try {
                String json = redisTemplate.opsForValue().get(parsedKey);
                if (json != null) {
                    return objectMapper.readValue(json, returnType);
                }
            } catch (Exception e) {
                log.warn("[RedisCache] Cache read (double-check) failed for key '{}': {}", parsedKey, e.getMessage());
                return joinPoint.proceed();
            }

            Object result = joinPoint.proceed();

            if (result != null) {
                try {
                    redisTemplate.opsForValue().set(
                            parsedKey,
                            objectMapper.writeValueAsString(result),
                            redisCache.ttl(),
                            TimeUnit.MINUTES);
                } catch (Exception e) {
                    log.warn("[RedisCache] Failed to write cache for key '{}': {}", parsedKey, e.getMessage());
                }
            }
            return result;
        }
    }

    private String parse(ProceedingJoinPoint joinPoint, String keyExpression) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] params = parameterNameDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        EvaluationContext context = new StandardEvaluationContext();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                context.setVariable(params[i], args[i]);
            }
        }
        return expressionParser.parseExpression(keyExpression).getValue(context, String.class);
    }
}
