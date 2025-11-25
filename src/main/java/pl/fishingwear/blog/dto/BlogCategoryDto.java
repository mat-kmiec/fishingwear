package pl.fishingwear.blog.dto;

import pl.fishingwear.blog.model.BlogCategory;

public record BlogCategoryDto(
        Long id, String name, Long parentCategoryId, String parentCategoryName, String moderatorName, Long moderatorId

){
}
