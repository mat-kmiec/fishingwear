package pl.fishingwear.mapper;

import pl.fishingwear.dto.UserCredentialsDto;
import pl.fishingwear.model.User;

public class UserCredentialsDtoMapper {
    public static UserCredentialsDto toDto(User user){
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();
        return new UserCredentialsDto(email, password, role);
    }
}
