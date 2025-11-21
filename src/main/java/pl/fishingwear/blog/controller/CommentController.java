package pl.fishingwear.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.fishingwear.blog.service.CommentService;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}/status")
    public String updateCommentStatus(@PathVariable Long id,
                                      @RequestParam String status,
                                      HttpServletRequest request,
                                      Principal principal) {

        commentService.updateStatus(id, status, principal);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
