package com.allforone.starvestop.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockUtil {

    private final StringRedisTemplate lockRedisTemplate;

    //락을 획득하는 메서드 (락을 획득 할 수 있는지 없는지 검사)
    public boolean tryLock(String key, String value, long timeOutSecond) {

        //1. Redis에 해당 키가 있는지 없는지 검사

        //2. 키가 있으면 false 반환, 없으면 키를 생성하고 true 반환
        //lock:account:1 라는 이름의 key가 있는지 조사
        //있으면 false, 없으면 lock:account:1, UUID 이런 모양으로 생성, TTL은 default 5초
        Boolean result = lockRedisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(timeOutSecond));

        //키가 있으면 false -> 락을 획들 할 수 없음
        //키가 없으면 true -> 락을 획들 할 수 있음
        return Boolean.TRUE.equals(result);
    }


    //락을 해제하는 메서드
    public void unlock(String key, String value) {

        //내가 만든 키가 맞는지 확인
        //누가 만들었는지 담겨있는 value 가져와서
        //redis에 받아온 Key가 있는지 없는지 검사
        //있다면 value가 일치하는지 안하는지 검사
        //일치한다면 key 삭제
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "   return redis.call('del', KEYS[1]) " +
                        "else " +
                        "   return 0 " +
                        "end";

        lockRedisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                value
        );

        log.info("획득한 락 키 반납 :::: {}, UUID == {}", Thread.currentThread().getName(), value);
    }
}
