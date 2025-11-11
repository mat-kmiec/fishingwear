package pl.fishingwear.product.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fishingwear.product.model.Color;
import pl.fishingwear.product.repository.ColorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }
}