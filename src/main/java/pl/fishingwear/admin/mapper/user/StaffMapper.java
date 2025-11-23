package pl.fishingwear.admin.mapper.user;

import org.springframework.stereotype.Component;
import pl.fishingwear.admin.dto.user.StaffUserDto;
import pl.fishingwear.user.model.User;

@Component
public class StaffMapper {

    public static StaffUserDto toDto(User user){
        return new StaffUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getRole());
    }

}
