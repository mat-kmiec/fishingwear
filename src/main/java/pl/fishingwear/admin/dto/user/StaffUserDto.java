package pl.fishingwear.admin.dto.user;

import pl.fishingwear.user.model.Role;

public record StaffUserDto(Long id, String firstName, String lastName, Role role) {
}
