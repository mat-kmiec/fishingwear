package pl.fishingwear.theme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "THEME")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "COLOR_PRIMARY_HEX", nullable = false)
    private String colorPrimaryHex;

    @Column(name = "COLOR_SECONDARY_HEX", nullable = false)
    private String colorSecondaryHex;

    @Column(name = "COLOR_ACCENT_HEX", nullable = false)
    private String colorAccentHex;

}