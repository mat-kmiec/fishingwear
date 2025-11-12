package pl.fishingwear.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.cart.model.Cart;
import pl.fishingwear.user.model.User;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    Optional<Cart> findByGuestCartId(String guestCartId);

    Optional<Cart> findByUserId(Long userId);
}
