//package com.allforone.starvestop.config;
//
//import com.allforone.starvestop.common.dto.AuthUser;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithSecurityContextFactory;
//
//import java.util.List;
//
//public class WithMockAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthUser> {
//
//    @Override
//    public SecurityContext createSecurityContext(WithMockAuthUser annotation) {
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//
//        AuthUser authUser = new AuthUser(
//                annotation.userId(),
//                annotation.email(),
//                annotation.nickname(),
//                annotation.role()
//        );
//
//        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + annotation.role().name()));
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser, null, authorities);
//
//        context.setAuthentication(authenticationToken);
//        return context;
//    }
//}
