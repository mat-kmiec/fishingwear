package pl.fishingwear.slider.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.fishingwear.common.service.ImageService;
import pl.fishingwear.slider.model.SliderImage;
import pl.fishingwear.slider.repository.SliderImageRepository;

@Service
@AllArgsConstructor
public class SliderImageService {


    private static final String SLIDER_UPLOAD_DIR = "uploads/slider/";
    private final ImageService imageService;
    private final SliderImageRepository sliderImageRepository;

    /**
     * Zapisuje plik fizycznie i tworzy wpis w bazie danych.
     */
    @Transactional
    public SliderImage uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Plik nie może być pusty");
        }

        // 1. Zlecenie zapisu fizycznego do ImageService
        // Parametry dobrane pod Slider:
        // - generateMainFile = true (potrzebujemy dużego zdjęcia na stronę)
        // - width/height = 500x300 (to wymiary MINIATURY do panelu admina, nie głównego zdjęcia!)
        // - quality = wysoka dla obu
        String savedFileName = imageService.saveImage(
                file,
                SLIDER_UPLOAD_DIR,
                true,       // Generuj plik główny (1:1)
                500, 300,   // Rozmiar miniatury (thumb_) dla panelu admina
                0.8,        // Jakość miniatury
                0.95        // Jakość głównego zdjęcia (musi być ładne)
        );

        SliderImage sliderImage = new SliderImage();
        sliderImage.setFileName(savedFileName);
        sliderImage.setOriginalFileName(file.getOriginalFilename());

        return sliderImageRepository.save(sliderImage);
    }
}
