package com.allforone.starvestop.common.aspect;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.RedisLock;
import com.allforone.starvestop.common.utils.RedisLockUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class RedisLockAspect {

    private final RedisLockUtil redisLockUtil;

    @Around("@annotation(redisLock)")
    public Object run(ProceedingJoinPoint joinPoint,
                      RedisLock redisLock) throws Throwable {

        String keyPreFix = redisLock.key();
        String value = UUID.randomUUID().toString();

        Object[] args = joinPoint.getArgs();

        //RedisLock 어노테이션이 걸린 메서드의 첫 번쨰 파라미터를 가져옴
        Object id = args[0];
        String key = keyPreFix + ":" + id;

        //locked 값이 false면 키가 이미 있는 것
        //locked 값이 true면 키가 없는 것
        boolean locked = redisLockUtil.tryLock(key, value, redisLock.timeout());

        //locked가 false -> 락을 획드 실패하는 경우에 실행
        if (!locked) {
            throw new CustomException(ErrorCode.LOCK_GET_FAILED);
        }

        //locked가 true -> 락을 획득한 경우에 실행
        try {
            return joinPoint.proceed();
        } finally {
            redisLockUtil.unlock(key, value);
        }
    }
}
