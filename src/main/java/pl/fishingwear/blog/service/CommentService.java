package pl.fishingwear.blog.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.blog.exception.PostNotFoundException;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.repository.CommentRepository;
import pl.fishingwear.blog.repository.PostRepository;
import pl.fishingwear.common.exception.UserNotFoundException;
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
        commentRepository.save(comment);
    }

}
