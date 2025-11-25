package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.PostCreateDto;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.user.model.User;

import java.time.LocalDateTime;

@Component
public class PostCreateMapper {


    public Post toEntity(PostCreateDto dto, User author){
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());
        post.setStatus(PostStatus.PUBLISHED);
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);
        return post;
    }

}
