package pl.fishingwear.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.blog.dto.BlogCategorySidebarDto;
import pl.fishingwear.blog.dto.PostDetailsDto;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.dto.PostSideBarDto;
import pl.fishingwear.blog.exception.PostNotFoundException;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.blog.repository.PostRepository;
import pl.fishingwear.blog.service.BlogCategoryService;
import pl.fishingwear.blog.service.BlogService;
import pl.fishingwear.blog.service.CommentService;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.model.User;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final BlogCategoryService blogCategoryService;
    private final PostRepository postRepository;
    private final CommentService commentService;

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model, Authentication auth) {
        PostDetailsDto postDetailsDto = blogService.getPostById(id);
        model.addAttribute("post", postDetailsDto);
        Long currentUserId = null;
        if (auth != null && auth.isAuthenticated()) {
            try {
                String email = auth.getName();
                if (auth.getPrincipal() instanceof User user) {
                    currentUserId = user.getId();
                }
            } catch (Exception ignored) {}
        }
        model.addAttribute("currentUserId", currentUserId);
        return "blog/blog";
    }

    @PostMapping("/{id}/addComment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String comment,
                             Principal principal) {
        commentService.addComment(id, comment, principal);

        return "redirect:/blog/" + id;
    }


    @GetMapping
    public String getBlogPosts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<PostDto> postsPage = blogService.getPosts(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                search,
                categoryId
        );

        model.addAttribute("postsPage", postsPage);
        model.addAttribute("currentPage", postsPage.getNumber());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);

        return "blog/blog-list";
    }



    @ModelAttribute("sidebarCategories")
    public List<BlogCategorySidebarDto> getSidebarCategories() {
        return blogCategoryService.getAllCategoriesWithPostCount();
    }

    @ModelAttribute("sidebarCategoriesCount")
    public int getSidebarCategoriesCount() {
        return Math.toIntExact(postRepository.countByStatus(PostStatus.PUBLISHED));
    }

    @ModelAttribute("recentPosts")
    public List<PostSideBarDto> getRecentPosts() {
        return blogService.getTop3Post();
    }


    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound() {
        return "redirect:/logowanie";
    }

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFound() {
        return "redirect:/blog";
    }



}
