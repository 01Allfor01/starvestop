package com.allforone.starvestop.domain.user.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFunction {

    private final UserRepository userRepository;

    public User getById(Long id) {
        return userRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedIsFalse(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public void existByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public User save(String userEmail, String password, String userName, String nickname) {
        User user = User.create(userEmail, password, userName, nickname);
        return userRepository.save(user);
    }

}
