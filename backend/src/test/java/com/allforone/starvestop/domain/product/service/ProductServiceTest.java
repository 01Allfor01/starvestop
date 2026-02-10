//package com.allforone.starvestop.domain.product.service;
//
//import com.allforone.starvestop.common.dto.AuthUser;
//import com.allforone.starvestop.domain.owner.entity.Owner;
//import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
//import com.allforone.starvestop.domain.product.dto.response.CreateProductResponse;
//import com.allforone.starvestop.domain.product.entity.Product;
//import com.allforone.starvestop.domain.product.enums.ProductStatus;
//import com.allforone.starvestop.domain.product.repository.ProductRepository;
//import com.allforone.starvestop.domain.store.entity.Store;
//import com.allforone.starvestop.domain.store.enums.StoreCategory;
//import com.allforone.starvestop.domain.store.enums.StoreStatus;
//import com.allforone.starvestop.domain.store.repository.StoreRepository;
//import com.allforone.starvestop.domain.user.enums.UserRole;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.Point;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.math.BigDecimal;
//import java.time.LocalTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @InjectMocks
//    private ProductService productService;
//
//    private AuthUser authUser;
//    private Store store;
//
//    @BeforeEach
//    void setUp() {
//        authUser = AuthUser.of(1L, "lee@seo.jun", "leeseojun", UserRole.OWNER);
//        Owner owner = Owner.create("lee@seo.jun", "password", "leeseojun");
//        ReflectionTestUtils.setField(owner, "id", 1L);
//        Point point = new Point(new Coordinate(127.0016985,37.5642135)).setSRID(4326);
//        store = Store.create(owner, "올포원 가게", "내배캠", "이곳은 내배캠", StoreCategory.KOREAN_FOOD,
//                point, LocalTime.now(), LocalTime.now().plusHours(9), StoreStatus.OPENED, "사업자 등록 번호");
//        ReflectionTestUtils.setField(store, "id", 1L);
//    }
//
//    @Test
//    @DisplayName("상품_생성_성공")
//    void createProduct_success() {
//        //given
//        CreateProductRequest request = new CreateProductRequest();
//        ReflectionTestUtils.setField(request, "storeId", 1L);
//        ReflectionTestUtils.setField(request, "productName", "두바이 쫀득 쿠키");
//        ReflectionTestUtils.setField(request, "description", "이걸 안먹어?");
//        ReflectionTestUtils.setField(request, "stock", 500L);
//        ReflectionTestUtils.setField(request, "price", new BigDecimal("8000"));
//        ReflectionTestUtils.setField(request, "salePrice", new BigDecimal("7000"));
//        ReflectionTestUtils.setField(request, "status", ProductStatus.SALE);
//
//        Product product = Product.create(
//                store,
//                request.getName(),
//                request.getDescription(),
//                request.getPrice(),
//                request.getSalePrice(),
//                request.getStock(),
//                request.getStatus());
//        ReflectionTestUtils.setField(product, "id", 3L);
//
//        when(storeRepository.findByIdAndIsDeletedIsFalse(1L)).thenReturn(Optional.of(store));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        //when
//        CreateProductResponse response = productService.createProduct(authUser, request);
//
//        //then
//        assertThat(response.getId()).isEqualTo(3L);
//        assertThat(response.getName()).isEqualTo("두바이 쫀득 쿠키");
//    }
//}