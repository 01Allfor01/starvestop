package com.allforone.starvestop.domain.product.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.request.UpdateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.CreateProductResponse;
import com.allforone.starvestop.domain.product.dto.response.GetProductDetailResponse;
import com.allforone.starvestop.domain.product.dto.response.UpdateProductResponse;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.common.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ProductService productService;

    private AuthUser authUser;
    private Store store;
    private Product product;

    @BeforeEach
    void setUp() {
        authUser = AuthUser.of(1L, "lee@seo.jun", "leeseojun", UserRole.OWNER);
        Owner owner = Owner.create("lee@seo.jun", "password", "leeseojun");
        ReflectionTestUtils.setField(owner, "id", 1L);

        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = gf.createPoint(new Coordinate(127.001695, 37.5642135));

        store = Store.create(owner, "올포원 가게", "내배캠", "이곳은 내배캠", StoreCategory.KOREAN_FOOD,
                point, LocalTime.now(), LocalTime.now().plusHours(9), StoreStatus.OPENED, "사업자 등록 번호");
        ReflectionTestUtils.setField(store, "id", 1L);

        product = Product.create(store, "하리보 젤리", "난 빨간 곰이 제일 좋아 왜냐면 난 딸기가 제일 좋거든",
                new BigDecimal("4000"), new BigDecimal("2000"), 500, ProductStatus.GENERAL);
        ReflectionTestUtils.setField(product, "id", 3L);
        ReflectionTestUtils.setField(product, "imageUuid", "img-uuid-123");
    }

    @Test
    @DisplayName("상품_생성_성공")
    void createProduct_success() {
        //given
        CreateProductRequest request = new CreateProductRequest();
        ReflectionTestUtils.setField(request, "storeId", 1L);
        ReflectionTestUtils.setField(request, "name", "두바이 쫀득 쿠키");
        ReflectionTestUtils.setField(request, "description", "이걸 안먹어?");
        ReflectionTestUtils.setField(request, "stock", 500);
        ReflectionTestUtils.setField(request, "price", new BigDecimal("8000"));
        ReflectionTestUtils.setField(request, "salePrice", new BigDecimal("7000"));
        ReflectionTestUtils.setField(request, "status", ProductStatus.SALE);

        Product product = Product.create(
                store,
                request.getName(),
                request.getDescription(),
                request.getPrice(), request.getSalePrice(),
                request.getStock(),
                request.getStatus());
        ReflectionTestUtils.setField(product, "id", 3L);

        when(storeService.getById(1L)).thenReturn(store);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //when
        CreateProductResponse response = productService.createProduct(authUser, request);

        //then
        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("두바이 쫀득 쿠키");
    }

    @Test
    @DisplayName("상품_상세_조회_성공")
    void getProductDetail_success() {
        //given
        when(productRepository.findByIdAndIsDeletedIsFalse(3L)).thenReturn(Optional.of(product));
        when(s3Service.createPresignedGetUrl(
                eq(3L),
                eq(S3BucketStatus.PRODUCT),
                eq("img-uuid-123")
        )).thenReturn("https://presigned-url/product/3");

        //when
        GetProductDetailResponse response = productService.getProductDetail(3L);

        //then
        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("하리보 젤리");
        assertThat(response.getImageUrl()).isEqualTo("https://presigned-url/product/3");

        verify(productRepository).findByIdAndIsDeletedIsFalse(3L);
        verify(s3Service).createPresignedGetUrl(3L, S3BucketStatus.PRODUCT, "img-uuid-123");
    }

    @Test
    @DisplayName("상품_수정_성공")
    void updateProduct_success() {
        //given
        UpdateProductRequest request = new UpdateProductRequest();
        ReflectionTestUtils.setField(request, "name", "애플 시나몬 와플");
        ReflectionTestUtils.setField(request, "description", "이것이 사과인가 와플인가 수원 왕 애플 와플");
        ReflectionTestUtils.setField(request, "stock", 123);
        ReflectionTestUtils.setField(request, "price", new BigDecimal("9000"));
        ReflectionTestUtils.setField(request, "salePrice", new BigDecimal("8500"));
        ReflectionTestUtils.setField(request, "status", ProductStatus.SALE);

        when(productRepository.findByIdAndIsDeletedIsFalse(3L)).thenReturn(Optional.of(product));

        //when
        UpdateProductResponse response = productService.updateProduct(authUser, 3L, request);

        //then
        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("애플 시나몬 와플");

        verify(productRepository).findByIdAndIsDeletedIsFalse(3L);
        verify(productRepository).flush();
    }

    @Test
    @DisplayName("상품_수정_실패_FORBIDDEN_다른_사장일때")
    void updateProduct_fail_forbidden_otherOwner() {
        //given
        AuthUser otherOwner = AuthUser.of(2L, "lee@jun.beom", "또다른 나 이준범", UserRole.OWNER);

        UpdateProductRequest request = new UpdateProductRequest();
        ReflectionTestUtils.setField(request, "name", "이 상품이 네 상품이냐 관상으로 보아하니");

        when(productRepository.findByIdAndIsDeletedIsFalse(3L))
                .thenReturn(Optional.of(product));

        //when
        CustomException exception =
                assertThrows(CustomException.class,
                        () -> productService.updateProduct(otherOwner, 3L, request));

        //then
        assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());

        verify(productRepository).findByIdAndIsDeletedIsFalse(3L);
        verify(productRepository, never()).flush();
    }

    @Test
    @DisplayName("상품_삭제_성공_OWNER")
    void delete_success_owner() {
        //given
        when(productRepository.findByIdAndIsDeletedIsFalse(3L)).thenReturn(Optional.of(product));

        //when
        productService.delete(authUser, 3L);

        //then
        Object isDeleted = ReflectionTestUtils.getField(product, "isDeleted");
        if (isDeleted != null) {
            assertThat((Boolean) isDeleted).isTrue();
        }

        verify(productRepository).findByIdAndIsDeletedIsFalse(3L);
    }
}