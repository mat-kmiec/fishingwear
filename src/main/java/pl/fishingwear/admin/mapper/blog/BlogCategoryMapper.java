package pl.fishingwear.admin.mapper.blog;

import org.springframework.stereotype.Component;
import pl.fishingwear.admin.dto.blog.BlogCategoryDto;
import pl.fishingwear.blog.model.BlogCategory;

@Component
public class BlogCategoryMapper {

    public static BlogCategoryDto toDto(BlogCategory blogCategory){
        return new BlogCategoryDto(blogCategory.getId(), blogCategory.getName(), blogCategory.getParent(), blogCategory.getAssignedModerator().getFirstName());
    }

}
