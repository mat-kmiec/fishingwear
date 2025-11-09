package pl.fishingwear.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCredentialsDto {
    private final String email;
    private final String password;
    private final String role;
}
