package pl.fishingwear.theme.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.theme.dto.ThemeCreateDto;
import pl.fishingwear.theme.dto.ThemeDto;
import pl.fishingwear.theme.exception.ThemeNotFoundException;
import pl.fishingwear.theme.mapper.ThemeMapper;
import pl.fishingwear.theme.model.Theme;
import pl.fishingwear.theme.repository.ThemeRepository;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ThemeService {

    private static final Long DEFAULT_THEME_ID = 0L;
    private final UserRepository userRepository;

    private ThemeRepository themeRepository;


    public List<ThemeDto> getAllThemes() {
        return themeRepository.findAllByOrderByIdAsc()
                .stream()
                .map(ThemeMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteTheme(Long themeToDeleteId) {
        if (themeToDeleteId.equals(DEFAULT_THEME_ID)) {
            throw new IllegalArgumentException("Nie mozna usunac motywu domyslnego!");
        }
        Theme themeToDelete = themeRepository.findById(themeToDeleteId).orElseThrow(ThemeNotFoundException::new);
        Theme defaultTheme = themeRepository.findById(DEFAULT_THEME_ID).orElseThrow(IllegalStateException::new);

        // Update all users with theme to delete
        List<User> usersToUpdate = userRepository.findAllBySelectedThemeId(themeToDeleteId);
        usersToUpdate.forEach(user -> user.setSelectedTheme(defaultTheme));
        userRepository.saveAll(usersToUpdate);

        // delete theme
        themeRepository.delete(themeToDelete);
    }


    public void createTheme(ThemeCreateDto dto) {
        themeRepository.save(ThemeMapper.toEntity(dto));
    }

    @Transactional
    public void updateTheme(ThemeDto dto) {
        Theme theme = themeRepository.findById(dto.id()).orElseThrow(ThemeNotFoundException::new);
        theme.setName(dto.name());
        theme.setColorPrimaryHex(dto.colorPrimaryHex());
        theme.setColorSecondaryHex(dto.colorSecondaryHex());
        theme.setColorAccentHex(dto.colorAccentHex());
        themeRepository.save(theme);
    }

    @Transactional
    public void changeUserTheme(String username, Long newThemeId)
            throws UsernameNotFoundException, ThemeNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));


        Theme selectedTheme = themeRepository.findById(newThemeId).orElseThrow(ThemeNotFoundException::new);

        user.setSelectedTheme(selectedTheme);
        userRepository.save(user);

    }

    public ThemeDto getCurrentUserThemeData(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika."));

        Theme selectedTheme = user.getSelectedTheme();

        if (selectedTheme == null) {
            return this.getThemeDtoById(DEFAULT_THEME_ID);
        }

        return ThemeMapper.toDto(selectedTheme);
    }

    private ThemeDto getThemeDtoById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(ThemeNotFoundException::new);

        return ThemeMapper.toDto(theme);
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(ThemeNotFoundException::new);
    }

    public ThemeDto getDefaultThemeData() {
        try {
            return this.getThemeDtoById(DEFAULT_THEME_ID);
        } catch (ThemeNotFoundException e) {
            return new ThemeDto(1L, "Domyślny Awaryjny", "#343a40", "#6c757d", "#adb5bd");
        }
    }
}
