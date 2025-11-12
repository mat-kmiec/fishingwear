package pl.fishingwear.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.cart.model.Cart;
import pl.fishingwear.cart.model.CartItem;
import pl.fishingwear.product.model.ProductVariant;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);
}
