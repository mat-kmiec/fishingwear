package pl.fishingwear.slider.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.slider.dto.SliderItemDto;
import pl.fishingwear.slider.model.Slider;
import pl.fishingwear.slider.model.SliderImage;
import pl.fishingwear.slider.model.SliderItem;
import pl.fishingwear.slider.repository.SliderImageRepository;
import pl.fishingwear.slider.repository.SliderRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SliderService {

    private final SliderRepository sliderRepository;
    private final SliderImageRepository sliderImageRepository;


    @Transactional
    public void createSlider(String name) {
        Slider slider = new Slider();
        slider.setName(name);
        slider.setActive(false);
        sliderRepository.save(slider);
    }

    @Transactional
    public void deleteSlider(Long id) {
        sliderRepository.deleteById(id);
    }

    @Transactional
    public void activateSlider(Long id) {
        sliderRepository.deactivateAllSliders();
        Slider slider = sliderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono slidera"));
        slider.setActive(true);
        sliderRepository.save(slider);
    }

    public List<Slider> getAllSliders() {
        return sliderRepository.findAll();
    }

    public Slider getSlider(Long id) {
        return sliderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono slidera"));
    }

    @Transactional
    public void updateSliderContent(Long sliderId, List<SliderItemDto> newItems) {
        Slider slider = getSlider(sliderId);
        slider.getItems().clear();
        for (int i = 0; i < newItems.size(); i++) {
            SliderItemDto dto = newItems.get(i);

            SliderImage image = sliderImageRepository.findById(dto.getImageId())
                    .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe ID zdjęcia"));

            SliderItem item = new SliderItem();
            item.setTitle("");
            item.setDescription(dto.getDescription());
            item.setSortOrder(i);
            item.setImage(image);
            slider.addItem(item);
        }

        sliderRepository.save(slider);
    }
}
