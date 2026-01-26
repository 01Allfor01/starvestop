package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreFunction {

    private final StoreRepository storeRepository;

    public Store getById(Long id) {
        return storeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );
    }
}
