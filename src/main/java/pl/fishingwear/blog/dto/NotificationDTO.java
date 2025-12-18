package pl.fishingwear.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fishingwear.blog.model.enums.NotificationType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private String targetUrl;
    private NotificationType type;
    private boolean read;
    private LocalDateTime createdAt;
    private Long postId;
    private Long categoryId;
}