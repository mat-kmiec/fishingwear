package pl.fishingwear.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.common.exception.EmailCannotBeSendException;
import pl.fishingwear.cart.dto.CartItemDto;
import pl.fishingwear.cart.dto.CartViewDto;
import pl.fishingwear.common.service.MailService;
import pl.fishingwear.order.dto.AdminOrderDetailsDto;
import pl.fishingwear.order.dto.CheckoutFormDto;
import pl.fishingwear.order.dto.OrderConfirmationDto;
import pl.fishingwear.cart.model.Cart;
import pl.fishingwear.order.dto.OrderDto;
import pl.fishingwear.order.exception.OrderNotFoundException;
import pl.fishingwear.order.mapper.OrderMapper;
import pl.fishingwear.order.model.Order;
import pl.fishingwear.order.model.OrderItem;
import pl.fishingwear.cart.repository.CartRepository;
import pl.fishingwear.order.model.OrderStatus;
import pl.fishingwear.order.repository.OrderRepository;
import pl.fishingwear.cart.service.CartService;
import pl.fishingwear.product.model.ProductVariant;
import pl.fishingwear.product.repository.ProductVariantRepository;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.service.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private static final BigDecimal SHIPPING_COST_COURIER = new BigDecimal("15.99");
    private static final BigDecimal SHIPPING_COST_PARCEL_LOCKER = new BigDecimal("12.99");
    private static final BigDecimal DEFAULT_SHIPPING_COST = new BigDecimal("15.99");
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final MailService mailService;


    @Transactional(rollbackFor = Exception.class)
    public Long placeOrder(CheckoutFormDto dto) {

        CartViewDto cartView = cartService.getCurrentCartView();
        if (cartView.items().isEmpty()) {
            throw new RuntimeException("Koszyk jest pusty.");
        }

        Cart cart = cartService.getOrCreateCart();
        Optional<User> currentUserOpt = cartService.getCurrentUser();
        List<OrderItem> orderItems = new ArrayList<>();
        List<ProductVariant> variantsToUpdate = new ArrayList<>();

        for (CartItemDto itemDto : cartView.items()) {
            ProductVariant variant = productVariantRepository.findById(itemDto.productVariantId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono produktu o ID: " + itemDto.productVariantId()));

            if (variant.getQuantity() < itemDto.quantity()) {
                throw new RuntimeException("Niewystarczająca ilość produktu: " + itemDto.productName());
            }

            variant.setQuantity(variant.getQuantity() - itemDto.quantity());
            variantsToUpdate.add(variant);
            OrderItem orderItem = buildOrderItem(itemDto, variant);
            orderItems.add(orderItem);
        }

        BigDecimal shippingCost = calculateShippingCost(dto.shippingMethod());
        BigDecimal total = cartView.subtotalPrice().add(shippingCost);
        Order order = Order.builder()
                .user(currentUserOpt.orElse(null))
                .guestCartId(currentUserOpt.isEmpty() ? cart.getGuestCartId() : null)
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phone(dto.phone())
                .shippingAddress(dto.shippingAddress())
                .zipCode(dto.zipCode())
                .city(dto.city())
                .shippingMethod(dto.shippingMethod())
                .paymentMethod(dto.paymentMethod())
                .subtotal(cartView.subtotalPrice())
                .shippingCost(shippingCost)
                .total(total)
                .status(OrderStatus.NEW)
                .build();

        orderItems.forEach(order::addItem);
        Order savedOrder = orderRepository.save(order);
        productVariantRepository.saveAll(variantsToUpdate);
        clearUserCart(cart);
        try{
            mailService.sendOrderConfirmationEmail(order.getEmail(), order.getId());
        }catch (Exception e){
            throw new EmailCannotBeSendException();
        }
        return savedOrder.getId();
    }

    private OrderItem buildOrderItem(CartItemDto itemDto, ProductVariant variant) {
        return OrderItem.builder()
                .productVariant(variant)
                .productName(itemDto.productName())
                .quantity(itemDto.quantity())
                .unitPrice(itemDto.unitPrice())
                .totalPrice(itemDto.totalPrice())
                .build();
    }

    private BigDecimal calculateShippingCost(String shippingMethod) {
        if (shippingMethod.contains("Paczkomat")) {
            return SHIPPING_COST_PARCEL_LOCKER;
        }
        if (shippingMethod.contains("Kurier")) {
            return SHIPPING_COST_COURIER;
        }
        return DEFAULT_SHIPPING_COST;
    }

    private void clearUserCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public OrderConfirmationDto getOrderForConfirmation(Long orderId) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia"));

        checkOrderAccess(order);

        return new OrderConfirmationDto(
                order.getId(),
                order.getSubtotal(),
                order.getShippingCost(),
                order.getTotal(),
                order.getPaymentMethod()
        );
    }

    private void checkOrderAccess(Order order) throws AccessDeniedException {
        Optional<User> currentUserOpt = cartService.getCurrentUser();

        if (currentUserOpt.isPresent()) {
            User currentUser = currentUserOpt.get();
            if (order.getUser() == null || !order.getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Brak dostępu do tego zamówienia");
            }
        } else {
            String guestCartId = cartService.getGuestCartIdFromCookie()
                    .orElseThrow(() -> new AccessDeniedException("Brak dostępu do tego zamówienia"));

            if (order.getGuestCartId() == null || !order.getGuestCartId().equals(guestCartId)) {
                throw new AccessDeniedException("Brak dostępu do tego zamówienia");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findOrdersForUser(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return orderRepository.findAllByUserOrderByCreatedAt(user)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminOrderDetailsDto findOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        checkOrderAccess(order);

        return orderMapper.toAdminDto(order);
    }

}