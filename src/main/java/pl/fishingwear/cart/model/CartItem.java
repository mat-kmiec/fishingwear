package pl.fishingwear.cart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.fishingwear.product.model.ProductVariant;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cart_item_cart_variant", columnNames = {"cart_id", "product_variant_id"})
})
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "added_at", updatable = false, nullable = false)
    private LocalDateTime addedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        if (id == null) {
            return cart.equals(cartItem.cart) &&
                    productVariant.equals(cartItem.productVariant);
        }
        return id.equals(cartItem.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return java.util.Objects.hash(cart, productVariant);
        }
        return java.util.Objects.hash(id);
    }
}
