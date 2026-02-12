package com.allforone.starvestop.domain.owner.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.owner.dto.UpdateOwnerRequest;
import com.allforone.starvestop.domain.owner.dto.UpdateOwnerResponse;
import com.allforone.starvestop.domain.owner.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.OWNER_UPDATE_SUCCESS;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PatchMapping
    public ResponseEntity<CommonResponse<UpdateOwnerResponse>> updateOwner(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateOwnerRequest request
    ) {
        Long ownerId = authUser.getUserId();
        UpdateOwnerResponse response = ownerService.updateOwner(ownerId, request);

        CommonResponse<UpdateOwnerResponse> result = CommonResponse.success(OWNER_UPDATE_SUCCESS, response);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteOwner(@AuthenticationPrincipal AuthUser authUser) {
        Long ownerId = authUser.getUserId();
        ownerService.deleteOwner(ownerId);

        CommonResponse<Void> result = CommonResponse.successNoData(SuccessMessage.OWNER_DELETE_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
