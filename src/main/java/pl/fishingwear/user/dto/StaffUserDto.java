package pl.fishingwear.user.dto;

import pl.fishingwear.user.model.Role;

public record StaffUserDto(Long id, String firstName, String lastName, Role role) {
}
