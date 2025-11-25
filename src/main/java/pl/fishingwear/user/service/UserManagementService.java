package pl.fishingwear.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.fishingwear.user.dto.EditUserRequest;
import pl.fishingwear.user.dto.StaffUserDto;
import pl.fishingwear.user.mapper.StaffMapper;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.model.Role;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {


    private final UserRepository userRepository;

    public Page<User> getUsers(int page, int size, String search) {
        if (search == null || search.isBlank()) {
            return userRepository.findAll(PageRequest.of(page, size));
        }

        return userRepository.searchUsers(search.toLowerCase(), PageRequest.of(page, size));
    }

    public void updateUser(EditUserRequest request) {

        if(request.getId() == 1){
            throw new IllegalArgumentException("Nie można usunąć użytkownika systemowego (ID=1).");
        }
        User user = userRepository.findById(request.getId())
                .orElseThrow(UserNotFoundException::new);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole()));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    public List<StaffUserDto> getAllStaff(){
        return userRepository.findByRoleIn(List.of(Role.ADMIN, Role.MODERATOR))
                .stream()
                .map(StaffMapper::toDto)
                .toList();
    }
}
