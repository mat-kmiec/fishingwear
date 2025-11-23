package pl.fishingwear.admin.mapper.blog;

import org.springframework.stereotype.Component;
import pl.fishingwear.admin.dto.blog.AdminCommentDto;
import pl.fishingwear.blog.model.Comment;

@Component
public class CommentMapper {
    public static AdminCommentDto toDto(Comment comment){
        return new AdminCommentDto(comment.getId(),
                comment.getAuthor().getFirstName() + comment.getAuthor().getLastName(),
                comment.getAuthor().getEmail(),
                comment.getContent(),
                comment.getPost().getTitle(),
                comment.getCreatedAt());
    }

}
