package pl.fishingwear.blog.dto;

import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailsDto(
        Long id,
        String title,
        String content,
        String img,
        PostStatus status,
        Long authorId,
        String authorName,
        BlogCategory category,
        LocalDateTime createdAt,
        List<Comment> comments
) {}
