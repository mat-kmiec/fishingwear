package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.PostSideBarDto;
import pl.fishingwear.blog.model.Post;

@Component
public class PostSideBarMapper {
    public static PostSideBarDto toDto(Post post) {
        return new PostSideBarDto(post.getId(), post.getTitle(), post.getCreatedAt());
    }
}
