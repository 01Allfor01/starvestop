package com.allforone.starvestop.common.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {
    String key();                   //Lock key
    long waitTime() default 5L;     //락 획득을 위한 대기 시간
    long leaseTime() default 3L;    //락 획득 시 갖고있을 시간
}
