package pl.fishingwear.blog.dto;

import java.time.LocalDateTime;

public record AdminCommentDto(
        Long id,
        String authorName,
        String authorEmail,
        String content,
        String postTitle,
        LocalDateTime submissionDate
) {
}
