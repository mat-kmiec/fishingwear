package pl.fishingwear.user.mapper;

import pl.fishingwear.user.dto.UserCredentialsDto;
import pl.fishingwear.user.model.User;

public class UserCredentialsDtoMapper {
    public static UserCredentialsDto toDto(User user){
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();
        return new UserCredentialsDto(email, password, role);
    }
}
