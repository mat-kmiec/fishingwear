package pl.fishingwear.blog.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.blog.exception.PostNotFoundException;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.CommentStatus;
import pl.fishingwear.blog.repository.CommentRepository;
import pl.fishingwear.blog.repository.PostRepository;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.model.Role;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;
import pl.fishingwear.user.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional
    public void addComment(Long postId, String commentContent, Principal principal){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.findByEmail(principal.getName());
        if(commentContent == null || commentContent.isBlank()){
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        Comment comment = Comment.builder()
                .content(commentContent)
                .post(post)
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();
        boolean userIsStaff = user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR);
        if (userIsStaff){
            comment.setStatus(CommentStatus.APPROVED);
        }
        commentRepository.save(comment);
    }

    @Transactional
    public void updateStatus(Long commentId, String statusStr, Principal principal) {
        var currentUser = userService.findByEmail(principal.getName());
        boolean isStaff = currentUser.getRole().equals(Role.ADMIN) ||
                currentUser.getRole().equals(Role.MODERATOR);

        if (!isStaff) {
            throw new org.springframework.security.access.AccessDeniedException("Brak uprawnień");
        }

        CommentStatus newStatus;
        try {
            newStatus = CommentStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nieznany status: " + statusStr);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono komentarza"));

        if (newStatus == CommentStatus.REJECTED) {
            commentRepository.delete(comment);
        } else {
            comment.setStatus(newStatus);
            commentRepository.save(comment);
        }
    }

}
