package pl.fishingwear.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.fishingwear.slider.dto.SliderDto;
import pl.fishingwear.slider.model.Slider;
import pl.fishingwear.slider.model.SliderItem;
import pl.fishingwear.slider.repository.SliderRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final SliderRepository sliderRepository;

    public HomeController(SliderRepository sliderRepository) {
        this.sliderRepository = sliderRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        Slider activeSlider = sliderRepository.findByIsActiveTrue().orElse(null);

        List<SliderDto> itemsDto = Collections.emptyList();

        if (activeSlider != null) {
            List<SliderItem> entities = activeSlider.getItems();
            entities.sort(Comparator.comparing(SliderItem::getSortOrder));
            itemsDto = entities.stream()
                    .map(item -> new SliderDto(
                            item.getTitle(),
                            item.getDescription(),
                            item.getImage() != null ? item.getImage().getFileName() : "placeholder.jpg"
                    ))
                    .collect(Collectors.toList());
        }

        model.addAttribute("sliderItems", itemsDto);



        return "home";
    }
}
