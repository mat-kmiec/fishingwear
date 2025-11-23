package pl.fishingwear.theme.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.theme.dto.ThemeCreateDto;
import pl.fishingwear.theme.dto.ThemeDto;
import pl.fishingwear.theme.model.Theme;

@Component
public class ThemeMapper {

    public static ThemeDto toDto(Theme theme){
        return new ThemeDto(theme.getId(), theme.getName(), theme.getColorPrimaryHex(), theme.getColorSecondaryHex(), theme.getColorAccentHex());
    }

    public static Theme toEntity(ThemeCreateDto dto){
        return new Theme(null, dto.name(), dto.colorPrimaryHex(), dto.colorSecondaryHex(), dto.colorAccentHex());
    }
}
