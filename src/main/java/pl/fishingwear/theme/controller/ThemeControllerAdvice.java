package pl.fishingwear.theme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.fishingwear.theme.dto.ThemeDto;
import pl.fishingwear.theme.service.ThemeService;
import pl.fishingwear.user.service.UserService;

@ControllerAdvice
@RequiredArgsConstructor
public class ThemeControllerAdvice {

    private final UserService userService;
    private final ThemeService themeService;


    @ModelAttribute("themeData")
    public ThemeDto addThemeDataToModel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() &&
                !auth.getPrincipal().equals("anonymousUser")) {

            String username = auth.getName();
            try {
                ThemeDto dto = themeService.getCurrentUserThemeData(username);
                return dto;
            } catch (Exception e) {
                return themeService.getDefaultThemeData();
            }
        }
        return themeService.getDefaultThemeData();
    }
}
