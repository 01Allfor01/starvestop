package com.allforone.starvestop.domain.user.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.PasswordEncoder;
import com.allforone.starvestop.domain.user.dto.response.GetUserResponse;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
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

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 조회
    @Transactional(readOnly = true)
    public GetUserResponse getUser(Long userId) {
        User user = getUserOrThrow(userId);

        String imageUrl = s3Service.createPresignedGetUrl(user.getId(), S3BucketStatus.USER, user.getImageUuid());

        return GetUserResponse.from(user, imageUrl);
    }

    // 회원 정보 수정
    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        String password = request.getPassword();
        String nickname = request.getNickname();

        User foundUser = getUserOrThrow(userId);

        foundUser.update(nickname, passwordEncoder.encode(password));

        return new UpdateUserResponse(
                foundUser.getEmail(),
                foundUser.getNickname(),
                foundUser.getUsername()
        );
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User foundUser = getUserOrThrow(userId);

        foundUser.delete();
    }

    @Transactional
    public User getUserOrThrow(Long userId) {
        return userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public User getKakaoUserOrElseSignUp(Long providerId) {
        return userRepository.findByProviderIdAndIsDeletedIsFalse(providerId);
    }

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

    public User saveKakao(Long providerId, String userEmail, String password, String userName, String nickname) {
        User user = User.createKakao(userEmail, password, userName, nickname, providerId);
        return userRepository.save(user);
    }
}
