package pl.fishingwear.blog.dto;

import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.user.model.User;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String title,
        String content,
        String img,
        PostStatus status,
        Long authorId,
        String authorName,
        BlogCategory category,
        LocalDateTime createdAt
   ) {}
