package pl.fishingwear.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.blog.service.CommentService;
import pl.fishingwear.blog.service.CommentVoteService;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.service.UserService;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final CommentVoteService commentVoteService;

    @PostMapping("/{id}/status")
    public String updateCommentStatus(@PathVariable Long id,
                                      @RequestParam String status,
                                      HttpServletRequest request,
                                      Principal principal) {

        commentService.updateStatus(id, status, principal);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }


    @PostMapping("/{id}/vote")
    public ResponseEntity<Integer> vote(
            @PathVariable Long id,
            @RequestParam boolean upvote
    ) {
        Long userId = userService.getCurrentUser()
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User must be logged in"));

        int newScore = commentVoteService.vote(id, userId, upvote);

        return ResponseEntity.ok(newScore);
    }

}
