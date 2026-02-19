package com.allforone.starvestop.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AesGcmBillingKeyCrypto implements BillingKeyCrypto {

    private static final int IV_LEN = 12;
    private static final int TAG_BIT_LEN = 128;

    private final SecretKeySpec keySpec;
    private final SecureRandom random = new SecureRandom();

    public AesGcmBillingKeyCrypto(@Value("${app.crypto.billing-key-base64}") String base64Key) {
        byte[] key = Base64.getDecoder().decode(base64Key);
        this.keySpec = new SecretKeySpec(key, "AES");
    }

    @Override
    public String encrypt(String plain) {
        try {
            byte[] iv = new byte[IV_LEN];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(TAG_BIT_LEN, iv));

            byte[] cipherText = cipher.doFinal(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            byte[] out = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(cipherText, 0, out, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new RuntimeException("billingKey 인코딩 실패", e);
        }
    }

    @Override
    public String decrypt(String encrypted) {
        try {
            byte[] in = Base64.getDecoder().decode(encrypted);

            byte[] iv = new byte[IV_LEN];
            byte[] cipherText = new byte[in.length - IV_LEN];
            System.arraycopy(in, 0, iv, 0, IV_LEN);
            System.arraycopy(in, IV_LEN, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(TAG_BIT_LEN, iv));

            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("billingKey 디코딩 실패", e);
        }
    }
}
