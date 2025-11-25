package pl.fishingwear.slider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SliderDto {
    private String title;
    private String description;
    private String imageName;
}