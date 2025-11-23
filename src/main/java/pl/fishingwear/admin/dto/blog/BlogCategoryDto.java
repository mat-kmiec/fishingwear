package pl.fishingwear.admin.dto.blog;

import pl.fishingwear.blog.model.BlogCategory;

public record BlogCategoryDto(
        Long id, String name, BlogCategory parentCategory, String moderator

){
}
