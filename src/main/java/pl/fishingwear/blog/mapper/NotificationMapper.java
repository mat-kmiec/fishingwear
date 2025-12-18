package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.NotificationDTO;
import pl.fishingwear.blog.model.Notification;

@Component
public class NotificationMapper {

    public NotificationDTO toDto(Notification notification) {
        Long categoryId = null;
        if (notification.getCategory() != null) {
            categoryId = notification.getCategory().getId();
        } else if (notification.getPost() != null && notification.getPost().getCategory() != null) {
            categoryId = notification.getPost().getCategory().getId();
        }

        return NotificationDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .targetUrl(notification.getTargetUrl())
                .type(notification.getType())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .postId(notification.getPost() != null ? notification.getPost().getId() : null)
                .categoryId(categoryId)
                .build();
    }
}