package pl.fishingwear.slider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fishingwear.slider.model.Slider;

public interface SliderRepository extends JpaRepository<Slider, Long> {
}
