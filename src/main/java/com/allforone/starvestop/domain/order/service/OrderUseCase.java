package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.cart.service.CartFunction;
import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import com.allforone.starvestop.domain.coupon.service.UserCouponFunction;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.product.service.ProductService;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private final CartFunction cartFunction;
    private final OrderProductFunction orderProductFunction;
    private final UserCouponFunction userCouponFunction;
    private final UserFunction userFunction;
    private final StoreService storeService;
    private final ProductService productService;
    private final OrderService orderService;

    @Transactional
    public OrderResponse order(Long userId, Long storeId, Long userCouponId) {
        User user = userFunction.getById(userId);

        Store store = storeService.getById(storeId);

        UserCoupon userCoupon = getUserCoupon(userCouponId);

        List<Cart> cartList = cartFunction.findAllByUserIdAndStoreId(userId, storeId);

        cartListEmptyCheck(cartList);

        cartList.forEach(cart -> productService.decreaseById(cart.getProduct().getId(), cart.getQuantity()));

        BigDecimal amount = calculateAmount(cartList, userCoupon);

        Order order = orderService.createOrder(user, store, userCoupon, amount);

        orderProductFunction.saveAll(order, cartList);

        cartFunction.deleteAll(cartList);

        return OrderResponse.from(order);
    }

    private UserCoupon getUserCoupon(Long userCouponId) {
        if (userCouponId == null) {
            return null;
        }

        return userCouponFunction.getByIdAndNotUsed(userCouponId);
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

                    return unitPrice.multiply(BigDecimal.valueOf(cart.getQuantity()));})
                .reduce(BigDecimal.ZERO, BigDecimal::add);

         if (userCoupon!=null && amount.compareTo(userCoupon.getCoupon().getMinAmount()) <= 0) {
             userCoupon.use();
             return amount.subtract(userCoupon.getCoupon().getDiscountAmount());
         }
         return amount;
    }
}
