package pl.fishingwear.admin.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.user.model.User;

@Data
@AllArgsConstructor
public class PostCreateDto {
    private String title;
    private String content;
    private PostStatus status;
    private User author;
    private BlogCategory category;
}
