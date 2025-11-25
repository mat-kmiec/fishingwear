package pl.fishingwear.slider.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.fishingwear.slider.service.SliderImageService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminSliderController {
    private final SliderImageService sliderImageService;

    @GetMapping("/slider")
    public String slider() {
        return "admin/slider";
    }

    @GetMapping("/slider/edycja")
    public String sliderEdit() {
        return "admin/slider-edit";
    }




    @PostMapping("/slider/upload")
    public ResponseEntity<?> uploadSliderImage(@RequestParam("imageFiles") List<MultipartFile> imageFiles) {
        try {
            for (MultipartFile file : imageFiles) {
                sliderImageService.uploadImage(file);
            }

            return ResponseEntity.ok().body("{\"status\": \"success\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd zapisu");
        }
    }
}
