package pl.fishingwear.slider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fishingwear.slider.model.SliderImage;

public interface SliderImageRepository extends JpaRepository<SliderImage, Long> {
}
