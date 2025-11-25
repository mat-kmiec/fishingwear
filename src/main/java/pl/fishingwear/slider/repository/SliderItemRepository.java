package pl.fishingwear.slider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.slider.model.SliderItem;

@Repository
public interface SliderItemRepository extends JpaRepository<SliderItem, Long>{
}
