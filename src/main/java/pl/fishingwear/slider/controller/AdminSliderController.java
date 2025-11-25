package pl.fishingwear.slider.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.fishingwear.slider.dto.SliderItemDto;
import pl.fishingwear.slider.model.Slider;
import pl.fishingwear.slider.model.SliderItem;
import pl.fishingwear.slider.service.SliderImageService;
import pl.fishingwear.slider.service.SliderService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // Lepsze niż AllArgsConstructor przy wstrzykiwaniu zależności
public class AdminSliderController {

    private final SliderImageService sliderImageService;
    private final SliderService sliderService;

    // --- 1. WIDOK LISTY (Index) ---
    @GetMapping("/slider")
    public String slider(Model model) {
        // Przekazujemy listę sliderów do tabeli w HTML
        model.addAttribute("sliders", sliderService.getAllSliders());
        return "admin/slider";
    }

    // --- 2. WIDOK EDYCJI (Edit) ---
    @GetMapping("/slider/edycja/{id}")
    public String sliderEdit(@PathVariable Long id, Model model) {
        Slider slider = sliderService.getSlider(id);

        // Sortujemy elementy slidera wg kolejności (sortOrder),
        // żeby w panelu wyświetliły się tak, jak zostały zapisane.
        slider.getItems().sort(Comparator.comparing(SliderItem::getSortOrder));

        model.addAttribute("slider", slider);
        // Przekazujemy wszystkie dostępne zdjęcia do prawej kolumny (Bank zdjęć)
        model.addAttribute("galleryImages", sliderImageService.getAllImages());

        return "admin/slider-edit";
    }

    // --- 3. AKCJE FORMULARZY (Zwykłe POST) ---

    @PostMapping("/slider/create")
    public String createSlider(@RequestParam String name) {
        sliderService.createSlider(name);
        return "redirect:/admin/slider";
    }

    @PostMapping("/slider/delete/{id}")
    public String deleteSlider(@PathVariable Long id) {
        sliderService.deleteSlider(id);
        return "redirect:/admin/slider";
    }

    @PostMapping("/slider/activate/{id}")
    public String activateSlider(@PathVariable Long id) {
        sliderService.activateSlider(id);
        return "redirect:/admin/slider";
    }

    // --- 4. UPLOAD ZDJĘĆ (AJAX) ---
    @PostMapping("/slider/upload")
    public ResponseEntity<?> uploadSliderImage(@RequestParam("imageFiles") List<MultipartFile> imageFiles) {
        try {
            for (MultipartFile file : imageFiles) {
                sliderImageService.uploadImage(file);
            }
            return ResponseEntity.ok().body("{\"status\": \"success\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd zapisu: " + e.getMessage());
        }
    }

    @PostMapping("/slider/save-items/{id}")
    @ResponseBody // Ważne! Oznacza, że zwracamy JSON, a nie widok HTML
    public ResponseEntity<?> saveSliderItems(@PathVariable Long id, @RequestBody List<SliderItemDto> items) {
        try {
            sliderService.updateSliderContent(id, items);
            return ResponseEntity.ok().body("{\"status\": \"success\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd zapisu: " + e.getMessage());
        }
    }
}