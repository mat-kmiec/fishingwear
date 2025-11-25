package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.BlogCategoryDto;
import pl.fishingwear.blog.model.BlogCategory;

@Component
public class BlogCategoryMapper {

    public static BlogCategoryDto toDto(BlogCategory blogCategory){
        return new BlogCategoryDto(blogCategory.getId(), blogCategory.getName(), blogCategory.getParent(), blogCategory.getAssignedModerator().getFirstName());
    }

}
