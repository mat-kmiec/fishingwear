package pl.fishingwear.admin.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.admin.dto.BlogCategoryDto;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.Comment;

import java.util.List;

@Component
public class BlogCategoryMapper {

    public static BlogCategoryDto toDto(BlogCategory blogCategory){
        return new BlogCategoryDto(blogCategory.getId(), blogCategory.getName(), blogCategory.getParent(), blogCategory.getAssignedModerator().getFirstName());
    }
}
