package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.cart.service.CartService;
import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import com.allforone.starvestop.domain.coupon.service.UserCouponService;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.service.PaymentService;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.product.service.ProductService;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserCouponService userCouponService;
    private final UserService userService;
    private final StoreService storeService;
    private final ProductService productService;
    private final OrderProductService orderProductService;
    private final PaymentService paymentService;

    @Transactional
    public OrderResponse order(Long userId, Long storeId, Long userCouponId) {
        User user = userService.getById(userId);

        Store store = storeService.getById(storeId);

        UserCoupon userCoupon = getUserCoupon(userCouponId);

        List<Cart> cartList = cartService.findAllByUserIdAndStoreId(userId, storeId);

        cartListEmptyCheck(cartList);

        cartList.forEach(cart -> productService.decreaseById(cart.getProduct().getId(), cart.getQuantity()));

        BigDecimal amount = calculateAmount(cartList, userCoupon);

        Order order = orderService.createOrder(user, store, userCoupon, amount);

        orderProductService.saveAll(order, cartList);

        cartService.deleteAll(cartList);

        return OrderResponse.from(order);
    }

    private UserCoupon getUserCoupon(Long userCouponId) {
        if (userCouponId == null) {
            return null;
        }

        return userCouponService.getByIdAndNotUsed(userCouponId);
    }

    private void cartListEmptyCheck(List<Cart> cartList) {
        if (cartList.isEmpty()) {
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }
    }

    private BigDecimal calculateAmount(List<Cart> cartList, UserCoupon userCoupon) {
        BigDecimal amount = cartList.stream()
                .map(cart -> {
                    Product product = cart.getProduct();
                    BigDecimal unitPrice = product.getStatus() == ProductStatus.GENERAL
                            ? product.getPrice()
                            : product.getSalePrice();

                    return unitPrice.multiply(BigDecimal.valueOf(cart.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (userCoupon != null && amount.compareTo(userCoupon.getCoupon().getMinAmount()) >= 0) {
            userCoupon.use();
            return amount.subtract(userCoupon.getCoupon().getDiscountAmount());
        }
        return amount;
    }

    @Transactional
    public int cancelExpiredOrders(LocalDateTime now) {

        List<Order> expired = orderService.getExpiredOrder(now);
        int processed = 0;

        for (Order order : expired) {

            // 1. 주문 취소 (이미 PENDING인 것만 조회되므로 안전)
            order.cancel();

            // 2. 재고 반환


            // 3. 쿠폰 복구
            UserCoupon userCoupon = order.getUserCoupon();
            if (userCoupon != null) {
                userCoupon.restore();
            }

            // 4. 결제 취소 (주문키 1개 = 결제 1개)
            Optional<Payment> optionalPayment =
                    paymentService.findByOrderKey(order.getOrderKey());

            if (optionalPayment.isPresent()) {
                Payment payment = optionalPayment.get();

                if (payment.getStatus() == PaymentStatus.REQUESTED
                        || payment.getStatus() == PaymentStatus.PENDING) {

                    payment.cancel(); // 내부에서 requireStatus + 이벤트 적재
                }
            }

            processed++;
        }

        return processed;
    }
}
