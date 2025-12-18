package pl.fishingwear.blog.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.blog.dto.NotificationDTO;
import pl.fishingwear.blog.mapper.NotificationMapper;
import pl.fishingwear.blog.model.Notification;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.NotificationType;
import pl.fishingwear.blog.repository.NotificationRepository;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    public Page<NotificationDTO> getNotificationsForUser(String email, Pageable pageable) {
        User user = getUserByEmail(email);
        Page<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user, pageable);

        return notifications.map(notificationMapper::toDto);
    }

    public long countUnreadNotifications(String email) {
        User user = getUserByEmail(email);
        return notificationRepository.countByRecipientAndReadFalse(user);
    }

//    @Transactional
//    public void markAllAsRead(String email) {
//        User user = getUserByEmail(email);
//        notificationRepository.markAllAsReadForUser(user);
//    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Transactional
    public void createNotification(User recipient, String title, String message,
                                   NotificationType type, String targetUrl, Post post) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .targetUrl(targetUrl)
                .post(post)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        notificationRepository.save(notification);
    }
}