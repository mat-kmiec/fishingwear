package pl.fishingwear.blog.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.user.model.User; // Dodaj import dla User, jeśli jest potrzebny, ale zależy to od struktury DTO
import java.util.Optional;

@Component
public class PostMapper {

    public PostDto toDto(Post post){
        // Użycie Optional dla bezpiecznego dostępu do Author i jego pól
        User author = post.getAuthor();

        // 1. Bezpieczne uzyskanie ID autora
        Long authorId = Optional.ofNullable(author)
                .map(User::getId)
                .orElse(null); // Zwraca null, jeśli autor jest null

        // 2. Bezpieczne uzyskanie pełnej nazwy autora
        String authorFullName = Optional.ofNullable(author)
                .map(a -> a.getFirstName() + " " + a.getLastName())
                .orElse("Nieznany Autor"); // Zwraca domyślny ciąg, jeśli autor jest null

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImg(),
                post.getStatus(),
                authorId,            // Użycie bezpiecznego ID
                authorFullName,      // Użycie bezpiecznej nazwy
                post.getCategory(),
                post.getCreatedAt()
        );
    }
}