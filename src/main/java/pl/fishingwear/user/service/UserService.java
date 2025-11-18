package pl.fishingwear.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.dto.UserCredentialsDto;
import pl.fishingwear.user.dto.UserRegistrationDto;
import pl.fishingwear.common.exception.EmailAlreadyExistException;
import pl.fishingwear.common.exception.PasswordNotMatchException;
import pl.fishingwear.user.mapper.UserCredentialsDtoMapper;
import pl.fishingwear.user.model.Role;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::toDto);
    }

    @Transactional
    public void register(UserRegistrationDto registration) {
        // TODO: validate registration data
        String email = registration.getEmail().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistException(email);
        }
        if(!registration.getPassword().equals(registration.getConfirmPassword())){
            throw new PasswordNotMatchException();
        }
        User user = new User();
        user.setEmail(email);
        String passwordHash = passwordEncoder.encode(registration.getPassword());
        user.setPassword(passwordHash);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public void updateUserData(String email, User updatedData) {
        User user = findByEmail(email);
        user.setFirstName(updatedData.getFirstName());
        user.setLastName(updatedData.getLastName());
        user.setPhoneNumber(updatedData.getPhoneNumber());
        userRepository.save(user);
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            return userRepository.findByEmail(springUser.getUsername());
        }

        if (principal instanceof User appUser) {
            return Optional.of(appUser);
        }

        return Optional.empty();
    }


}
