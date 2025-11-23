package pl.fishingwear.admin.dto.blog;

public record BlogCategoryCreationDto(String name, Long parentCategoryId, Long moderatorId) {}
