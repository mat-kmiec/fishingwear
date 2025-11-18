package pl.fishingwear.blog.dto;

import java.time.LocalDateTime;

public record PostSideBarDto (
        Long id, String title, LocalDateTime createdAt
){
}
