package com.allforone.starvestop.domain.s3.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.service.ProductService;
import com.allforone.starvestop.domain.s3.dto.request.UploadImageRequest;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserService userService;
    private final StoreService storeService;
    private final ProductService productService;

    //사용자 이미지 업로드 완료
    @Transactional
    public void uploadUserImage(Long userId, UploadImageRequest request) {
        User user = userService.getUserOrThrow(request.getId());

        if (!userId.equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        user.uploadImageUrl(request.getUuid());
    }

    //매장 이미지 업로드 완료
    @Transactional
    public void uploadStoreImage(AuthUser authUser, UploadImageRequest request) {
        Store store = storeService.getStore(request.getId());

        storeService.idMismatchCheck(authUser.getUserId(), store);

        store.uploadImageUrl(request.getUuid());
    }

    //상품 이미지 업로드 완료
    @Transactional
    public void uploadProductImage(AuthUser authUser, UploadImageRequest request) {
        Product product = productService.getProductOrThrow(request.getId());

        productService.checkPermission(authUser, product.getStore().getOwner().getId());

        product.uploadImageUrl(request.getUuid());
    }


}
