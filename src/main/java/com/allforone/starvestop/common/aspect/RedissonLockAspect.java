package com.allforone.starvestop.common.aspect;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.RedissonLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(com.allforone.starvestop.common.utils.RedissonLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

//        if (method.getDeclaringClass().isInterface()) {
//            method = joinPoint
//                    .getTarget()
//                    .getClass()
//                    .getDeclaredMethod(method.getName(), method.getParameterTypes());
//        }

        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        String keyPreFix = redissonLock.key();

        Object[] args = joinPoint.getArgs();
        Object id = args[0];

        //RedissonLock 어노테이션이 걸린 메서드의 첫 번쨰 파라미터를 가져옴
        String key = keyPreFix + ":" + id;

        RLock lock = redissonClient.getLock(key);

        //locked 값이 false면 키가 이미 있는 것
        //locked 값이 true면 키가 없는 것
        try {
            boolean locked = lock.tryLock(
                    redissonLock.waitTime(),
                    redissonLock.leaseTime(),
                    TimeUnit.SECONDS);

            if (!locked) {
                throw new CustomException(ErrorCode.LOCK_GET_FAILED);
            }

            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}
