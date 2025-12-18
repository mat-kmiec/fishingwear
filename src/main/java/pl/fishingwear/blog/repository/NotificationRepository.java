package pl.fishingwear.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.blog.model.Notification;
import pl.fishingwear.user.model.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Pobiera wszystkie powiadomienia dla danego użytkownika (stronicowane)
    Page<Notification> findByRecipientOrderByCreatedAtDesc(User recipient, Pageable pageable);

    // Liczy nieprzeczytane, aby wyświetlić kropkę/badge w menu
    long countByRecipientAndReadFalse(User recipient);
}