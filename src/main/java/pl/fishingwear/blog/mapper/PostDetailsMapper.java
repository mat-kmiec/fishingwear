package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.PostDetailsDto;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.Post;

import java.util.List;

@Component
public class PostDetailsMapper {
    public PostDetailsDto toDto(Post post, List<Comment> comments){
        return new PostDetailsDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImg(),
                post.getStatus(),
                post.getAuthor().getId(),
                post.getAuthor().getFirstName(),
                post.getCategory(),
                post.getCreatedAt(),
                comments);
    }
}
