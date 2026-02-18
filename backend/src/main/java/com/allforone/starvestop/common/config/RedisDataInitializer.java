package com.allforone.starvestop.common.config;

import com.allforone.starvestop.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDataInitializer implements ApplicationRunner {

    private final StoreService storeService;

    @Override
    public void run(ApplicationArguments args) {
        storeService.syncStoresToRedis();
    }
}
