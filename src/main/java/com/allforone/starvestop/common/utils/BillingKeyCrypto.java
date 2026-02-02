package com.allforone.starvestop.common.utils;

public interface BillingKeyCrypto {
    String encrypt(String plain);
    String decrypt(String encrypted);
}
