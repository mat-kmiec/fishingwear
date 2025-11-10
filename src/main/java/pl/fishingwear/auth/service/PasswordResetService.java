package pl.fishingwear.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.fishingwear.auth.model.PasswordResetToken;
import pl.fishingwear.user.model.User;
import pl.fishingwear.auth.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetToken createTokenForUser(User user) {
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        return tokenRepository.save(resetToken);
    }

    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public Optional<PasswordResetToken> getValidToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredTokens() {
        var expiredTokens = tokenRepository.findAll().stream()
                .filter(t -> t.getExpiryDate().isBefore(LocalDateTime.now()))
                .toList();

        if (!expiredTokens.isEmpty()) {
            tokenRepository.deleteAll(expiredTokens);
        }
    }
}
