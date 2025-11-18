package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.model.Post;

@Component
public class PostMapper {
    public PostDto toDto(Post post){
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getImg(), post.getStatus(), post.getAuthor().getId(), post.getAuthor().getFirstName() + post.getAuthor().getLastName(), post.getCategory(), post.getCreatedAt());
    }

}
