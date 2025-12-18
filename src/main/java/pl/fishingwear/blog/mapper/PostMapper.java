package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.user.model.User; // Dodaj import dla User, jeśli jest potrzebny, ale zależy to od struktury DTO
import java.util.Optional;

@Component
public class PostMapper {

    public PostDto toDto(Post post){
        User author = post.getAuthor();

        Long authorId = Optional.ofNullable(author)
                .map(User::getId)
                .orElse(null);

        String authorFullName = Optional.ofNullable(author)
                .map(a -> a.getFirstName() + " " + a.getLastName())
                .orElse("Nieznany Autor");

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImg(),
                post.getStatus(),
                authorId,
                authorFullName,
                post.getCategory(),
                post.getCreatedAt()
        );
    }
}