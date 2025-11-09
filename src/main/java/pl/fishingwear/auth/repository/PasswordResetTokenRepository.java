package pl.fishingwear.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fishingwear.auth.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
