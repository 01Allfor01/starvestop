package com.allforone.starvestop.common.config;

import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JtsConfig {

    // point를 일정한 형태로 출력되게 하는 모듈
    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }
}
