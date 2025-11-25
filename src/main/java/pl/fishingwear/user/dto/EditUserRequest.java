package pl.fishingwear.user.dto;

import lombok.Data;

@Data
public class EditUserRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
