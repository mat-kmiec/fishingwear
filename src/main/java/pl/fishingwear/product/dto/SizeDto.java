package pl.fishingwear.product.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizeDto {
    private Long id;
    private String name;
    private String description;
}