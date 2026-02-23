//package com.allforone.starvestop.domain.payment.service;
//
//import com.allforone.starvestop.common.exception.CustomException;
//import com.allforone.starvestop.common.exception.ErrorCode;
//import com.allforone.starvestop.domain.order.entity.Order;
//import com.allforone.starvestop.domain.order.entity.OrderProduct;
//import com.allforone.starvestop.domain.order.enums.OrderStatus;
//import com.allforone.starvestop.domain.order.service.OrderProductService;
//import com.allforone.starvestop.domain.order.service.OrderService;
//import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
//import com.allforone.starvestop.domain.payment.entity.Payment;
//import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
//import com.allforone.starvestop.domain.payment.event.PaymentEventRelay;
//import com.allforone.starvestop.domain.product.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PaymentUsecaseTest {
//
//    @Mock private ProductService productService;
//    @Mock private OrderService orderService;
//    @Mock private OrderProductService orderProductService;
//    @Mock private PaymentService paymentService;
//    @Mock private PaymentEventRelay paymentEventRelay;
//
//    @InjectMocks private PaymentUsecase paymentUsecase;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // ===== createPayment =====
//
//    @Test
//    @DisplayName("createPayment: 주문 소유자가 아니면 FORBIDDEN")
//    void createPayment_forbidden_whenNotOwner() {
//        Long userId = 1L;
//        Long orderId = 10L;
//
//        Order order = mock(Order.class, RETURNS_DEEP_STUBS);
//        when(orderService.getForPayment(orderId)).thenReturn(order);
//        when(order.getUser().getId()).thenReturn(999L);
//
//        CustomException ex = assertThrows(CustomException.class,
//                () -> paymentUsecase.createPayment(userId, orderId));
//
//        assertEquals(ErrorCode.FORBIDDEN, ex.getErrorCode());
//        verify(paymentService, never()).saveAndFlush(any());
//        verify(paymentEventRelay, never()).relayFrom(any());
//    }
//
//    @Test
//    @DisplayName("createPayment: 기존 Payment가 있으면 그대로 반환")
//    void createPayment_returnsExisting_whenAlreadyExists() {
//        Long userId = 1L;
//        Long orderId = 10L;
//
//        Order order = mock(Order.class, RETURNS_DEEP_STUBS);
//        when(orderService.getForPayment(orderId)).thenReturn(order);
//        when(order.getUser().getId()).thenReturn(userId);
//        when(order.getOrderKey()).thenReturn("ok_123");
//        when(order.getAmount()).thenReturn(BigDecimal.valueOf(1000));
//
//        Payment existing = mock(Payment.class);
//        when(paymentService.findByOrderKey("ok_123")).thenReturn(Optional.of(existing));
//
//        CreatePaymentResponse res = paymentUsecase.createPayment(userId, orderId);
//
//        assertNotNull(res);
//        verify(paymentService, never()).saveAndFlush(any());
//        verify(paymentEventRelay, never()).relayFrom(any());
//    }
//
//    @Test
//    @DisplayName("createPayment: 신규 생성 성공 시 save + 이벤트 릴레이 호출")
//    void createPayment_savesAndRelays_whenNewPayment() {
//        Long userId = 1L;
//        Long orderId = 10L;
//
//        Order order = mock(Order.class, RETURNS_DEEP_STUBS);
//        when(orderService.getForPayment(orderId)).thenReturn(order);
//        when(order.getUser().getId()).thenReturn(userId);
//        when(order.getOrderKey()).thenReturn("ok_123");
//        when(order.getAmount()).thenReturn(BigDecimal.valueOf(1000));
//
//        when(paymentService.findByOrderKey("ok_123")).thenReturn(Optional.empty());
//
//        Payment payment = mock(Payment.class);
//
//        try (MockedStatic<Payment> mocked = Mockito.mockStatic(Payment.class)) {
//            mocked.when(() -> Payment.request(eq(userId), eq(order), eq("ok_123"), eq(BigDecimal.valueOf(1000))))
//                    .thenReturn(payment);
//
//            CreatePaymentResponse res = paymentUsecase.createPayment(userId, orderId);
//
//            assertNotNull(res);
//            verify(paymentService).saveAndFlush(payment);
//            verify(payment).markRequestedEvent();
//            verify(paymentEventRelay).relayFrom(payment);
//        }
//    }
//
//    @Test
//    @DisplayName("createPayment: 동시성으로 DataIntegrityViolationException 발생 시 기존 payment 반환")
//    void createPayment_returnsFound_whenDataIntegrityViolation() {
//        Long userId = 1L;
//        Long orderId = 10L;
//
//        Order order = mock(Order.class, RETURNS_DEEP_STUBS);
//        when(orderService.getForPayment(orderId)).thenReturn(order);
//        when(order.getUser().getId()).thenReturn(userId);
//        when(order.getOrderKey()).thenReturn("ok_123");
//        when(order.getAmount()).thenReturn(BigDecimal.valueOf(1000));
//        when(paymentService.findByOrderKey("ok_123")).thenReturn(Optional.empty());
//
//        Payment newPayment = mock(Payment.class);
//        Payment found = mock(Payment.class);
//
//        try (MockedStatic<Payment> mocked = Mockito.mockStatic(Payment.class)) {
//            mocked.when(() -> Payment.request(anyLong(), any(), anyString(), any()))
//                    .thenReturn(newPayment);
//
//            doThrow(new DataIntegrityViolationException("dup"))
//                    .when(paymentService).saveAndFlush(newPayment);
//
//            when(paymentService.getByOrderKey("ok_123")).thenReturn(found);
//
//            CreatePaymentResponse res = paymentUsecase.createPayment(userId, orderId);
//
//            assertNotNull(res);
//            verify(paymentService).getByOrderKey("ok_123");
//            // 저장 실패했으니 릴레이는 호출되면 안 됨
//            verify(paymentEventRelay, never()).relayFrom(any());
//        }
//    }
//
//    // ===== confirmSuccess =====
//
//    @Test
//    @DisplayName("confirmSuccess: 이미 성공(PAID or SUCCEEDED)이면 성공 redirect")
//    void confirmSuccess_returnsSuccessRedirect_whenAlreadyPaid() {
//        Payment payment = mock(Payment.class, RETURNS_DEEP_STUBS);
//        Order order = mock(Order.class);
//
//        when(paymentService.findByOrderKeyForUpdate("ok_123")).thenReturn(payment);
//        when(payment.getOrder().getId()).thenReturn(10L);
//        when(orderService.getForPayment(10L)).thenReturn(order);
//
//        when(order.getStatus()).thenReturn(OrderStatus.PAID);
//        when(payment.getStatus()).thenReturn(PaymentStatus.SUCCEEDED);
//
//        when(payment.getOrderKey()).thenReturn("ok_123");
//        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(1000));
//
//        String redirect = paymentUsecase.confirmSuccess("pk", "ok_123", 1000L);
//
//        assertTrue(redirect.startsWith("/success.html?orderKey=ok_123"));
//        verify(paymentEventRelay, never()).relayFrom(any());
//    }
//
//    @Test
//    @DisplayName("confirmSuccess: PENDING이면 ALREADY_PENDING 실패 redirect")
//    void confirmSuccess_returnsFail_whenPending() {
//        Payment payment = mock(Payment.class, RETURNS_DEEP_STUBS);
//        Order order = mock(Order.class);
//
//        when(paymentService.findByOrderKeyForUpdate("ok_123")).thenReturn(payment);
//        when(payment.getOrder().getId()).thenReturn(10L);
//        when(orderService.getForPayment(10L)).thenReturn(order);
//
//        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
//        when(payment.getStatus()).thenReturn(PaymentStatus.PENDING);
//
//        when(payment.getOrderKey()).thenReturn("ok_123");
//
//        String redirect = paymentUsecase.confirmSuccess("pk", "ok_123", 1000L);
//
//        assertTrue(redirect.contains("reason=ALREADY_PENDING"));
//        verify(paymentEventRelay, never()).relayFrom(any());
//    }
//
//    @Test
//    @DisplayName("confirmSuccess: 금액 불일치면 NON_RETRYABLE 실패 + 재고 반환(단, claimed=1일 때) + 실패 이벤트 릴레이")
//    void confirmSuccess_amountMismatch_releasesStockOnce_andRelaysFailEvent() {
//        Payment payment = mock(Payment.class, RETURNS_DEEP_STUBS);
//        Order order = mock(Order.class);
//
//        when(paymentService.findByOrderKeyForUpdate("ok_123")).thenReturn(payment);
//        when(payment.getOrder().getId()).thenReturn(10L);
//        when(orderService.getForPayment(10L)).thenReturn(order);
//
//        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
//        when(payment.getStatus()).thenReturn(PaymentStatus.REQUESTED);
//
//        when(payment.getOrderKey()).thenReturn("ok_123");
//        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(999));
//        when(payment.getOrder().getId()).thenReturn(10L);
//
//        when(paymentService.checkClaimed(payment)).thenReturn(1);
//        when(paymentService.toJson(any(Map.class))).thenReturn("{json}");
//
//        List<OrderProduct> orderProducts = List.of(op(101L, 2), op(202L, 1));
//        when(orderProductService.findListByOrderId(10L)).thenReturn(orderProducts);
//
//        String redirect = paymentUsecase.confirmSuccess("pk", "ok_123", 1000L);
//
//        assertTrue(redirect.contains("reason=AMOUNT_MISMATCH"));
//        verify(payment).failNonRetryable(anyString());
//        verify(productService).increaseById(101L, 2);
//        verify(productService).increaseById(202L, 1);
//        verify(payment).markStockReleased();
//        verify(paymentEventRelay).relayFrom(payment);
//    }
//
//    @Test
//    @DisplayName("confirmSuccess: PG confirm 성공이면 payment success + order paid + 성공 이벤트 릴레이 + 성공 redirect")
//    void confirmSuccess_pgSuccess_marksPaid_andRelaysSuccessEvent() {
//        Payment payment = mock(Payment.class, RETURNS_DEEP_STUBS);
//        Order order = mock(Order.class);
//
//        when(paymentService.findByOrderKeyForUpdate("ok_123")).thenReturn(payment);
//        when(payment.getOrder().getId()).thenReturn(10L);
//        when(orderService.getForPayment(10L)).thenReturn(order);
//
//        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
//        when(payment.getStatus()).thenReturn(PaymentStatus.REQUESTED);
//
//        when(payment.getOrderKey()).thenReturn("ok_123");
//        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(1000));
//
//        when(paymentService.tossApiConfirm(anyMap()))
//                .thenReturn(Map.of("status", "DONE"));
//
//        String redirect = paymentUsecase.confirmSuccess("pk_1", "ok_123", 1000L);
//
//        assertTrue(redirect.startsWith("/success.html?orderKey=ok_123"));
//        InOrder inOrder = inOrder(payment, paymentService, order, paymentEventRelay);
//        inOrder.verify(payment).pending();
//        inOrder.verify(paymentService).tossApiConfirm(anyMap());
//        inOrder.verify(payment).success("pk_1");
//        inOrder.verify(order).paid(any());
//        inOrder.verify(paymentEventRelay).relayFrom(payment);
//    }
//
//    @Test
//    @DisplayName("confirmSuccess: PG confirm 실패(5xx)면 FAILED_RETRYABLE + 재고반환X + 실패 redirect")
//    void confirmSuccess_pgFail_5xx_retryable() {
//        Payment payment = mock(Payment.class, RETURNS_DEEP_STUBS);
//        Order order = mock(Order.class);
//
//        when(paymentService.findByOrderKeyForUpdate("ok_123")).thenReturn(payment);
//        when(payment.getOrder().getId()).thenReturn(10L);
//        when(orderService.getForPayment(10L)).thenReturn(order);
//
//        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
//        when(payment.getStatus()).thenReturn(PaymentStatus.REQUESTED);
//        when(payment.getOrderKey()).thenReturn("ok_123");
//        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(1000));
//
//        when(paymentService.checkClaimed(payment)).thenReturn(1);
//        when(paymentService.toJson(any(WebClientResponseException.class))).thenReturn("{json}");
//
//        WebClientResponseException ex5xx =
//                WebClientResponseException.create(500, "ISE", null, new byte[]{}, StandardCharsets.UTF_8);
//
//        doThrow(ex5xx).when(paymentService).tossApiConfirm(anyMap());
//
//        String redirect = paymentUsecase.confirmSuccess("pk", "ok_123", 1000L);
//
//        assertTrue(redirect.contains("reason=PG_CONFIRM_FAILED"));
//        verify(payment).failRetryable(anyString());
//        verify(orderProductService, never()).findListByOrderId(anyLong());
//        verify(productService, never()).increaseById(anyLong(), anyInt());
//    }
//
//    @Test
//    @DisplayName("confirmSuccess: PG confirm 실패(4xx)면 FAILED_NON_RETRYABLE + 재고반환O(claimed=1) + 실패 redirect")
//    void confirmSuccess_pgFail_4xx_nonRetryable_releasesStock() {
//        Payment payment = mock(Payment.class, RETURNS_DEEP_STUBS);
//        Order order = mock(Order.class);
//
//        when(paymentService.findByOrderKeyForUpdate("ok_123")).thenReturn(payment);
//        when(payment.getOrder().getId()).thenReturn(10L);
//        when(orderService.getForPayment(10L)).thenReturn(order);
//
//        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
//        when(payment.getStatus()).thenReturn(PaymentStatus.REQUESTED);
//        when(payment.getOrderKey()).thenReturn("ok_123");
//        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(1000));
//        when(payment.getOrder().getId()).thenReturn(10L);
//
//        when(paymentService.checkClaimed(payment)).thenReturn(1);
//        when(paymentService.toJson(any(WebClientResponseException.class))).thenReturn("{json}");
//
//        List<OrderProduct> orderProducts = List.of(op(101L, 2));
//        when(orderProductService.findListByOrderId(10L)).thenReturn(orderProducts);
//
//        WebClientResponseException ex4xx =
//                WebClientResponseException.create(400, "BadRequest", null, new byte[]{}, StandardCharsets.UTF_8);
//
//        doThrow(ex4xx).when(paymentService).tossApiConfirm(anyMap());
//
//        String redirect = paymentUsecase.confirmSuccess("pk", "ok_123", 1000L);
//
//        assertTrue(redirect.contains("reason=PG_CONFIRM_FAILED"));
//        verify(payment).failNonRetryable(anyString());
//        verify(productService).increaseById(101L, 2);
//        verify(payment).markStockReleased();
//    }
//
//    @Test
//    @DisplayName("confirmSuccess: claimed!=1이면 실패 처리/재고반환이 중복 수행되지 않음")
//    void confirmSuccess_claimedNotOne_doesNothingOnFailurePath() {
//        Payment payment = mock(Payment.class, RETURNS_DEEP_STUBS);
//        Order order = mock(Order.class);
//
//        when(paymentService.findByOrderKeyForUpdate("ok_123")).thenReturn(payment);
//        when(payment.getOrder().getId()).thenReturn(10L);
//        when(orderService.getForPayment(10L)).thenReturn(order);
//
//        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
//        when(payment.getStatus()).thenReturn(PaymentStatus.REQUESTED);
//        when(payment.getOrderKey()).thenReturn("ok_123");
//        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(999));
//        when(payment.getOrder().getId()).thenReturn(10L);
//
//        when(paymentService.checkClaimed(payment)).thenReturn(0);
//        when(paymentService.toJson(any(Map.class))).thenReturn("{json}");
//
//        String redirect = paymentUsecase.confirmSuccess("pk", "ok_123", 1000L);
//
//        assertTrue(redirect.contains("reason=AMOUNT_MISMATCH"));
//        verify(payment, never()).failNonRetryable(anyString());
//        verify(orderProductService, never()).findListByOrderId(anyLong());
//        verify(productService, never()).increaseById(anyLong(), anyInt());
//        verify(paymentEventRelay, never()).relayFrom(any());
//    }
//
//    // ===== helper =====
//    private OrderProduct op(Long productId, int quantity) {
//        OrderProduct op = mock(OrderProduct.class);
//        when(op.getProductId()).thenReturn(productId);
//        when(op.getQuantity()).thenReturn(quantity);
//        return op;
//    }
//}
