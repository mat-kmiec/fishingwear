package pl.fishingwear.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "product_variant",
        indexes = {
                @Index(name = "idx_product_variant_product", columnList = "product_id"),
                @Index(name = "idx_product_variant_sku", columnList = "sku")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private Size size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;

    @Column(name = "sku", unique = true)
    private String sku;

    private BigDecimal price;

    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    private Integer quantity = 0;

    private Boolean isActive = true;
}