package com.allforone.starvestop.domain.owner.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.PasswordEncoder;
import com.allforone.starvestop.domain.owner.dto.UpdateOwnerRequest;
import com.allforone.starvestop.domain.owner.dto.UpdateOwnerResponse;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UpdateOwnerResponse updateOwner(Long ownerId, UpdateOwnerRequest request) {
        String password = request.getPassword();

        Owner owner = ownerRepository.findByIdAndIsDeletedIsFalse(ownerId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        owner.update(passwordEncoder.encode(password));

        ownerRepository.flush();

        return new UpdateOwnerResponse(
                owner.getEmail(),
                owner.getUsername()
        );
    }

    @Transactional
    public void deleteOwner(Long ownerId) {
        Owner owner = ownerRepository.findByIdAndIsDeletedIsFalse(ownerId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        owner.delete();

        ownerRepository.flush();
    }
}
