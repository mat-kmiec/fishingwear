package pl.fishingwear.cart.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.fishingwear.cart.dto.AddItemToCartDto;
import pl.fishingwear.cart.dto.CartItemDto;
import pl.fishingwear.cart.dto.CartViewDto;
import pl.fishingwear.cart.model.Cart;
import pl.fishingwear.cart.model.CartItem;
import pl.fishingwear.cart.repository.CartItemRepository;
import pl.fishingwear.cart.repository.CartRepository;
import pl.fishingwear.product.model.Color;
import pl.fishingwear.product.model.ProductImage;
import pl.fishingwear.product.model.ProductVariant;
import pl.fishingwear.product.model.Size;
import pl.fishingwear.product.repository.ProductVariantRepository;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    public static final String GUEST_CART_ID_COOKIE = "guest_cart_id";
    private static final int COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 30;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;

    public CartViewDto addItemToCart(AddItemToCartDto itemDto) {
        Cart cart = getOrCreateCart();
        ProductVariant variant = productVariantRepository.findById(itemDto.productVariantId())
                .orElseThrow(() -> new RuntimeException("Nie znaleziono wariantu produktu"));

        if (variant.getQuantity() < itemDto.quantity()) {
            throw new RuntimeException("Niewystarczająca ilość produktu w magazynie");
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProductVariant(cart, variant);

        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            int newQuantity = item.getQuantity() + itemDto.quantity();

            if (variant.getQuantity() < newQuantity) {
                throw new RuntimeException("Niewystarczająca ilość produktu w magazynie. Masz już " + item.getQuantity() + " w koszyku.");
            }
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductVariant(variant);
            newItem.setQuantity(itemDto.quantity());
            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToCartViewDto(savedCart);
    }

    @Transactional(readOnly = true)
    public CartViewDto getCurrentCartView() {
        Cart cart = getOrCreateCart();
        return mapToCartViewDto(cart);
    }


    @Transactional
    public Cart getOrCreateCart() {
        Optional<User> currentUser = getCurrentUser();

        if (currentUser.isPresent()) {

            return cartRepository.findByUser(currentUser.get())
                    .orElseGet(() -> createCartForUser(currentUser.get()));
        } else {

            String guestCartId = getGuestCartIdFromCookie().orElseGet(this::createGuestCartIdAndSetCookie);
            return cartRepository.findByGuestCartId(guestCartId)
                    .orElseGet(() -> createCartForGuest(guestCartId));
        }
    }

    private Cart createCartForUser(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private Cart createCartForGuest(String guestId) {
        Cart cart = new Cart();
        cart.setGuestCartId(guestId);
        return cartRepository.save(cart);
    }

    private String createGuestCartIdAndSetCookie() {
        String guestId = UUID.randomUUID().toString();
        setGuestCartIdCookie(guestId);
        return guestId;
    }

    public Optional<String> getGuestCartIdFromCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> GUEST_CART_ID_COOKIE.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void setGuestCartIdCookie(String guestId) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        Cookie cookie = new Cookie(GUEST_CART_ID_COOKIE, guestId);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        String username = authentication.getName();
        return userRepository.findByEmail(username);
    }


    private CartViewDto mapToCartViewDto(Cart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            return new CartViewDto(List.of(), 0, BigDecimal.ZERO);
        }

        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(this::mapToCartItemDto)
                .collect(Collectors.toList());

        BigDecimal subtotal = itemDtos.stream()
                .map(CartItemDto::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItemsCount = itemDtos.stream()
                .mapToInt(CartItemDto::quantity)
                .sum();

        return new CartViewDto(itemDtos, totalItemsCount, subtotal);
    }

    private CartItemDto mapToCartItemDto(CartItem item) {
        ProductVariant variant = productVariantRepository.findByIdWithDetails(item.getProductVariant().getId())
                .orElseThrow(() -> new RuntimeException("Wewnętrzny błąd: nie można załadować wariantu dla pozycji koszyka."));

        String productName = variant.getProduct().getName();
        String productSlug = variant.getProduct().getSlug();

        String imageUrl = variant.getProduct().getImages().stream()
                .filter(ProductImage::getIsMain)
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(variant.getProduct().getImages().isEmpty() ? "/images/default-placeholder.png" : variant.getProduct().getImages().get(0).getImageUrl());

        String size = Optional.ofNullable(variant.getSize()).map(Size::getName).orElse("-");
        String color = Optional.ofNullable(variant.getColor()).map(Color::getName).orElse("-");

        BigDecimal unitPrice = (variant.getDiscountPrice() != null && variant.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0)
                ? variant.getDiscountPrice() : variant.getPrice();

        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemDto(
                item.getId(),
                variant.getId(),
                productName,
                productSlug,
                imageUrl,
                size,
                color,
                item.getQuantity(),
                unitPrice,
                totalPrice
        );
    }

    @Transactional
    public CartViewDto removeItemFromCart(Long itemId) {
        Cart cart = getOrCreateCart();

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        Cart saved = cartRepository.save(cart);

        return mapToCartViewDto(saved);
    }

    @Transactional
    public CartViewDto updateItemQuantity(Long itemId, int newQuantity) {
        Cart cart = getOrCreateCart();

        cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    if (newQuantity <= 0) {
                        cart.removeItem(item);
                    } else if (item.getProductVariant().getQuantity() >= newQuantity) {
                        item.setQuantity(newQuantity);
                    } else {
                        throw new RuntimeException("Niewystarczająca ilość produktu w magazynie");
                    }
                });

        Cart saved = cartRepository.save(cart);
        return mapToCartViewDto(saved);
    }

    public void clearGuestCartCookie() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        Cookie cookie = new Cookie(GUEST_CART_ID_COOKIE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        assert response != null;
        response.addCookie(cookie);
    }

    @Transactional(readOnly = true)
    public int getCartItemCount() {
        try {
            Cart cart = getOrCreateCart();
            Integer count = cartItemRepository.sumQuantityByCartId(cart.getId());
            return (count == null) ? 0 : count;

        } catch (Exception e) {
            return 0;
        }
    }
}