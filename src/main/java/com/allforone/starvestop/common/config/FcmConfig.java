package com.allforone.starvestop.common.config;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FcmConfig {

    @Value("${fcm.secret-file}")
    private String secretFile;

    @PostConstruct
    public void initialize() {
        try{
            FileInputStream fis = new FileInputStream(secretFile);

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(fis);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            throw new CustomException(ErrorCode.SECRET_FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INVALID_SECRET_FILE);
        }
    }
}
