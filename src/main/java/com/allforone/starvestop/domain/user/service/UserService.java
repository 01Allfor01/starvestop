package com.allforone.starvestop.domain.user.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.PasswordEncoder;
import com.allforone.starvestop.domain.user.dto.request.UpdateUserRequest;
import com.allforone.starvestop.domain.user.dto.response.UpdateUserResponse;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 정보 수정
    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        String password = request.getPassword();
        String nickname = request.getNickname();

        User foundUser = userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        foundUser.update(nickname, passwordEncoder.encode(password));

        userRepository.flush();

        return new UpdateUserResponse(
                foundUser.getEmail(),
                foundUser.getNickname(),
                foundUser.getUsername()
        );
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User foundUser = userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        foundUser.delete();

        userRepository.flush();
    }
}
