package pl.fishingwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fishingwear.dto.UserCredentialsDto;
import pl.fishingwear.mapper.UserCredentialsDtoMapper;
import pl.fishingwear.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::toDto);
    }
}
