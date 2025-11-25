package pl.fishingwear.blog.dto;

public record BlogCategoryCreationDto(String name, Long parentCategoryId, Long moderatorId) {}
