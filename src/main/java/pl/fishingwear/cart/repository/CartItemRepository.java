package pl.fishingwear.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.fishingwear.cart.model.Cart;
import pl.fishingwear.cart.model.CartItem;
import pl.fishingwear.product.model.ProductVariant;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cart.id = :cartId")
    Integer sumQuantityByCartId(@Param("cartId") Long cartId);
}
