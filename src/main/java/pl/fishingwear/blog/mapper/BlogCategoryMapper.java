package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.BlogCategoryDto;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.user.model.User;

@Component
public class BlogCategoryMapper {

    public static BlogCategoryDto toDto(BlogCategory category) {

        BlogCategory parentEntity = category.getParent();

        Long parentId = parentEntity != null
                ? parentEntity.getId()
                : null;

        String parentName = parentEntity != null
                ? parentEntity.getName()
                : null;

        User moderatorEntity = category.getAssignedModerator();
        Long moderatorId = moderatorEntity != null
                ? moderatorEntity.getId()
                : null;

        String moderatorName = moderatorEntity != null
                ? moderatorEntity.getFirstName()
                : null;

        return new BlogCategoryDto(
                category.getId(),
                category.getName(),
                parentId,
                parentName,
                moderatorName,
                moderatorId
        );
    }
}
