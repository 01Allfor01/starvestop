//package com.allforone.starvestop.config;
//
//import com.allforone.starvestop.common.enums.UserRole;
//import org.springframework.security.test.context.support.WithSecurityContext;
//
//import java.lang.annotation.*;
//
//@Target({ElementType.METHOD, ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@WithSecurityContext(factory = WithMockAuthUserSecurityContextFactory.class)
//public @interface WithMockAuthUser {
//    long userId() default 1L;
//    String email() default "email@email.com";
//    String nickname() default "nickName";
//    UserRole role() default UserRole.USER;
//}
