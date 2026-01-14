package com.allforone.starvestop.domain.auth.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.JwtUtil;
import com.allforone.starvestop.common.utils.PasswordEncoder;
import com.allforone.starvestop.domain.auth.dto.request.SignInRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpRequest;
import com.allforone.starvestop.domain.auth.dto.response.SignInResponse;
import com.allforone.starvestop.domain.auth.dto.response.SignUpResponse;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.enums.UserRole;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        String userEmail = request.getEmail();
        String userName = request.getUsername();
        String nickname = request.getNickname();
        String password = request.getPassword();

        if (userRepository.existsByEmail(userEmail)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.create(userEmail, passwordEncoder.encode(password), UserRole.USER, userName, nickname);

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getEmail(), savedUser.getId(), savedUser.getRole());

        return new SignUpResponse(token);
    }

    public SignInResponse signIn(SignInRequest request) {
        String userEmail = request.getEmail();
        String password = request.getPassword();

        if (!userRepository.existsByEmail(userEmail)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User foundUser = userRepository.findByEmail(userEmail);

        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String token = jwtUtil.generateToken(foundUser.getUsername(), foundUser.getEmail(), foundUser.getId(), foundUser.getRole());

        return new SignInResponse(token);
    }
}
