package pl.fishingwear.theme.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.fishingwear.theme.dto.ThemeCreateDto;
import pl.fishingwear.theme.dto.ThemeDto;
import pl.fishingwear.theme.exception.ThemeNotFoundException;
import pl.fishingwear.theme.model.Theme;
import pl.fishingwear.theme.service.ThemeService;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.service.UserService;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/theme")
public class ThemeController {

    private final ThemeService themeService;
    private final UserService userService;

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

    @PostMapping("/update")
    public String updateTheme(@Valid @ModelAttribute("editTheme") ThemeDto dto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Wystąpiły błędy w formularzu edycji.");
            // W przypadku błędu najlepiej jest ponownie otworzyć modal lub użyć AJAX
            return "redirect:/admin/ustawienia";
        }

        try {
            themeService.updateTheme(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Motyw '" + dto.name() + "' został pomyślnie zaktualizowany.");
        } catch (ThemeNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie udało się zaktualizować motywu: nie znaleziono.");
        }

        return "redirect:/admin/ustawienia";
    }

    @PostMapping("/change")
    public String changeUserTheme(@RequestParam("themeId") Long newThemeId,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {

        String username = principal.getName();

        try {
            themeService.changeUserTheme(username, newThemeId);

            redirectAttributes.addFlashAttribute("successMessage", "Motyw został pomyślnie zmieniony.");

        } catch (UsernameNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: Nie znaleziono użytkownika.");
        } catch (ThemeNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: Wybrany motyw nie istnieje.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił nieoczekiwany błąd serwera.");
        }

        return "redirect:/ustawienia-profilu";
    }
}
