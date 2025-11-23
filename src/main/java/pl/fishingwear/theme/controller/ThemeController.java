package pl.fishingwear.theme.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.fishingwear.theme.dto.ThemeCreateDto;
import pl.fishingwear.theme.exception.ThemeNotFoundException;
import pl.fishingwear.theme.service.ThemeService;

@Controller
@AllArgsConstructor
@RequestMapping("/theme")
public class ThemeController {

    private final ThemeService themeService;

    @PostMapping("delete/{id}")
    public String deleteTheme(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            themeService.deleteTheme(id);
            redirectAttributes.addFlashAttribute("successMessage", "Motyw został pomyślnie usunięty.");
        } catch (ThemeNotFoundException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/ustawienia";
    }

    @PostMapping("/create")
    public String createTheme(@Valid ThemeCreateDto dto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Wystąpiły błędy w formularzu.");
            return "redirect:/admin/ustaweinia";
        }

        themeService.createTheme(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Motyw '" + dto.name() + "' został pomyślnie dodany.");

        return "redirect:/admin/ustawienia";
    }
}
