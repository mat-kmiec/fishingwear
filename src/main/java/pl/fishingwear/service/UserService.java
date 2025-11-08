package pl.fishingwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.dto.UserCredentialsDto;
import pl.fishingwear.dto.UserRegistrationDto;
import pl.fishingwear.exception.EmailAlreadyExistException;
import pl.fishingwear.mapper.UserCredentialsDtoMapper;
import pl.fishingwear.model.User;
import pl.fishingwear.repository.UserRepository;

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
        User user = new User();
        user.setEmail(email);
        String passwordHash = passwordEncoder.encode(registration.getPassword());
        user.setPassword(passwordHash);
        user.setRole("ROLE_USER");
        userRepository.save(user);

    }
}
