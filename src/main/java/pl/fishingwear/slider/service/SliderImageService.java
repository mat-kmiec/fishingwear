package pl.fishingwear.slider.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.fishingwear.common.service.ImageService;
import pl.fishingwear.slider.model.SliderImage;
import pl.fishingwear.slider.repository.SliderImageRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SliderImageService {

    private static final String SLIDER_UPLOAD_DIR = "uploads/slider/";

    private final ImageService imageService;
    private final SliderImageRepository sliderImageRepository;

    @Transactional
    public SliderImage uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Plik nie może być pusty");
        }

        String savedFileName = imageService.saveImage(
                file,
                SLIDER_UPLOAD_DIR,
                true,
                500, 300,
                0.8,
                0.95,
                true
        );

        SliderImage sliderImage = new SliderImage();
        sliderImage.setFileName(savedFileName);
        sliderImage.setOriginalFileName(file.getOriginalFilename());

        return sliderImageRepository.save(sliderImage);
    }

    public List<SliderImage> getAllImages() {
        return sliderImageRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}