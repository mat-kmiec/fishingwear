package pl.fishingwear.product.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "size",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_size_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;
}
