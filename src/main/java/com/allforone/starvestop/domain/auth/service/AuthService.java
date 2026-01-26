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
import com.allforone.starvestop.domain.owner.service.OwnerFunction;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserFunction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserFunction userFunction;
    private final OwnerFunction ownerFunction;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        String userEmail = request.getEmail();
        String userName = request.getUsername();
        String nickname = request.getNickname();
        String password = request.getPassword();

        userFunction.existByEmail(userEmail);

        User user = User.create(userEmail, passwordEncoder.encode(password), userName, nickname);

        User savedUser = userFunction.save(user);

        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getEmail(), savedUser.getId(), savedUser.getRole());

        return new SignUpResponse(token);
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        String userEmail = request.getEmail();
        String password = request.getPassword();

        User foundUser = userFunction.getByEmail(userEmail);

        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String token = jwtUtil.generateToken(foundUser.getUsername(), foundUser.getEmail(), foundUser.getId(), foundUser.getRole());

        return new SignInResponse(token);
    }

    @Transactional
    public SignUpResponse signUpOwner(@Valid SignUpOwnerRequest request) {
        String userEmail = request.getEmail();
        String userName = request.getUsername();
        String password = request.getPassword();

        ownerFunction.existsByEmail(userEmail);

        Owner savedOwner = ownerFunction.save(userEmail, passwordEncoder.encode(password), userName);

        String token = jwtUtil.generateToken(savedOwner.getUsername(), savedOwner.getEmail(), savedOwner.getId(), savedOwner.getRole());

        return new SignUpResponse(token);
    }

    @Transactional(readOnly = true)
    public SignInResponse signInOwner(SignInRequest request) {
        String userEmail = request.getEmail();
        String password = request.getPassword();

        Owner foundOwner = ownerFunction.findByEmail(userEmail);

        if (!passwordEncoder.matches(password, foundOwner.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String token = jwtUtil.generateToken(foundOwner.getUsername(), foundOwner.getEmail(), foundOwner.getId(), foundOwner.getRole());

        return new SignInResponse(token);
    }
}
