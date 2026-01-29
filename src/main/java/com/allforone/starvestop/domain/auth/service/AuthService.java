package com.allforone.starvestop.domain.auth.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.JwtUtil;
import com.allforone.starvestop.common.utils.PasswordEncoder;
import com.allforone.starvestop.domain.auth.dto.request.SignInRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpOwnerRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpRequest;
import com.allforone.starvestop.domain.auth.dto.response.SignInResponse;
import com.allforone.starvestop.domain.auth.dto.response.SignUpResponse;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.service.OwnerService;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.enums.UserRole;
import com.allforone.starvestop.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserService userService;
    private final OwnerService ownerService;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        String userEmail = request.getEmail();
        String userName = request.getUsername();
        String nickname = request.getNickname();
        String password = request.getPassword();

        userService.existByEmail(userEmail);

        User savedUser = userService.save(userEmail, passwordEncoder.encode(password), userName, nickname);

        String token = makeToken(savedUser.getUsername(), savedUser.getEmail(), savedUser.getId(), savedUser.getRole());

        return new SignUpResponse(token);
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        String userEmail = request.getEmail();
        String password = request.getPassword();

        User foundUser = userService.getByEmail(userEmail);

        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String token = makeToken(foundUser.getUsername(), foundUser.getEmail(), foundUser.getId(), foundUser.getRole());

        return new SignInResponse(token);
    }

    @Transactional
    public SignUpResponse signUpOwner(@Valid SignUpOwnerRequest request) {
        String userEmail = request.getEmail();
        String userName = request.getUsername();
        String password = request.getPassword();

        ownerService.existsByEmail(userEmail);

        Owner savedOwner = ownerService.save(userEmail, passwordEncoder.encode(password), userName);

        String token = makeToken(savedOwner.getUsername(), savedOwner.getEmail(), savedOwner.getId(), savedOwner.getRole());

        return new SignUpResponse(token);
    }

    @Transactional(readOnly = true)
    public SignInResponse signInOwner(SignInRequest request) {
        String userEmail = request.getEmail();
        String password = request.getPassword();

        Owner foundOwner = ownerService.findByEmail(userEmail);

        if (!passwordEncoder.matches(password, foundOwner.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String token = makeToken(foundOwner.getUsername(), foundOwner.getEmail(), foundOwner.getId(), foundOwner.getRole());

        return new SignInResponse(token);
    }

    public String makeToken(String name, String email, Long id, UserRole role) {
        return jwtUtil.generateToken(name, email, id, role);
    }

}
