package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.domain.store.dto.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.CreateStoreResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateStoreResponse createStore(Long userId, CreateStoreRequest request) {
        User user = userRepository.getReferenceById(userId);

        Point location = new Point(
                request.getLongitude(),
                request.getLatitude()
        );

        Store store = Store.create(
                user,
                request.getStoreName(),
                request.getAddress(),
                request.getDescription(),
                request.getCategory(),
                location,
                request.getOpenTime(),
                request.getCloseTime()
        );

        Store savedStore = storeRepository.save(store);
        return CreateStoreResponse.from(savedStore);
    }
}
