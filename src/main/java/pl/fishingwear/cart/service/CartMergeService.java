package pl.fishingwear.cart.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.cart.model.Cart;
import pl.fishingwear.cart.model.CartItem;
import pl.fishingwear.cart.repository.CartItemRepository;
import pl.fishingwear.cart.repository.CartRepository;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartMergeService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public void mergeCartsOnLogin(String userEmail, HttpServletRequest request, HttpServletResponse response) {

        User loggedInUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Błąd krytyczny: Nie można znaleźć użytkownika " + userEmail + " w transakcji scalania."));


        Optional<Cart> guestCartOpt = getGuestCartFromCookie(request);
        if (guestCartOpt.isEmpty()) {
            deleteGuestCartCookie(response);
            return;
        }

        Cart guestCart = guestCartOpt.get();
        guestCart.getItems().size();

        if (guestCart.getItems().isEmpty()) {
            cartRepository.delete(guestCart);
            deleteGuestCartCookie(response);
            return;
        }

        Cart userCart = cartRepository.findByUser(loggedInUser)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(loggedInUser);
                    return cartRepository.save(newCart);
                });

        userCart.getItems().size();

        if (guestCart.getId().equals(userCart.getId())) {
            guestCart.setGuestCartId(null);
            cartRepository.save(guestCart);
            deleteGuestCartCookie(response);
            return;
        }

        Set<CartItem> itemsToCopy = new HashSet<>(guestCart.getItems());

        for (CartItem guestItem : itemsToCopy) {
            Optional<CartItem> userItemOpt = userCart.getItems().stream()
                    .filter(item -> item.getProductVariant().getId().equals(guestItem.getProductVariant().getId()))
                    .findFirst();

            if (userItemOpt.isPresent()) {
                CartItem userItem = userItemOpt.get();
                int newQuantity = userItem.getQuantity() + guestItem.getQuantity();
                userItem.setQuantity(newQuantity);
                cartItemRepository.save(userItem);

            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(userCart);
                newCartItem.setProductVariant(guestItem.getProductVariant());
                newCartItem.setQuantity(guestItem.getQuantity());

                cartItemRepository.save(newCartItem);
                userCart.getItems().add(newCartItem);
            }
        }

        cartRepository.delete(guestCart);
        deleteGuestCartCookie(response);
    }

    private Optional<Cart> getGuestCartFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> CartService.GUEST_CART_ID_COOKIE.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .flatMap(cartRepository::findByGuestCartId);
    }

    private void deleteGuestCartCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(CartService.GUEST_CART_ID_COOKIE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}