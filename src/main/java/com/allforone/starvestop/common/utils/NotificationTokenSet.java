package com.allforone.starvestop.common.utils;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationTokenSet {
    private final Set<String> tokens = ConcurrentHashMap.newKeySet();

    public void add(String token) {
        tokens.add(token);
    }
    public void remove(String token) {
        tokens.remove(token);
    }
    public Set<String> getAll() {
        return Collections.unmodifiableSet(tokens);
    }
}
