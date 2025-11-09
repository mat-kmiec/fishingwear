package pl.fishingwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fishingwear.model.PasswordResetToken;
import pl.fishingwear.model.User;
import pl.fishingwear.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createTokenForUser(User user) {
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // 1 hour expiry time

        return tokenRepository.save(resetToken);
    }

    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}