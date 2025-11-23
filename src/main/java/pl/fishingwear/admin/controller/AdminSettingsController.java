package pl.fishingwear.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.fishingwear.theme.dto.ThemeCreateDto;
import pl.fishingwear.theme.dto.ThemeDto;
import pl.fishingwear.theme.service.ThemeService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminSettingsController {


    private final ThemeService themeService;

    public AdminSettingsController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/ustawienia")
    public String settings(Model model) {
        List<ThemeDto> themes = themeService.getAllThemes();
        model.addAttribute("themes", themes);
        model.addAttribute("newTheme", new ThemeCreateDto(null, null, null, null));

        return "admin/settings";
    }
}
