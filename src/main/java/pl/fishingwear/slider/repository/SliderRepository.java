package pl.fishingwear.slider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.fishingwear.slider.model.Slider;

import java.util.Optional;

public interface SliderRepository extends JpaRepository<Slider, Long> {
    Optional<Slider> findByIsActiveTrue();

    @Modifying
    @Query("UPDATE Slider s SET s.isActive = false")
    void deactivateAllSliders();
}
