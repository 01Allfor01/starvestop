package com.allforone.starvestop.domain.owner.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerFunction {

    private final OwnerRepository ownerRepository;

    public Owner getById(Long userId) {
        return ownerRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.OWNER_NOT_FOUND));
    }

    public void existsByEmail(String email) {
        if (ownerRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public Owner save(String userEmail, String password, String userName) {
        Owner owner = Owner.create(userEmail, password, userName);
        return ownerRepository.save(owner);
    }

    public Owner findByEmail(String email) {
        return ownerRepository.findByEmailAndIsDeletedIsFalse(email).orElseThrow(
                () -> new CustomException(ErrorCode.OWNER_NOT_FOUND));
    }
}
