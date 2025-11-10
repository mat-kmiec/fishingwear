package pl.fishingwear.product.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "color")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // np. "White"

    @Column(name = "hex_code")
    private String hexCode; // np. "#FFFFFF" – przydatne w UI
}
