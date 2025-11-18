package pl.fishingwear.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.blog.dto.BlogCategorySidebarDto;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.dto.PostSideBarDto;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.blog.repository.PostRepository;
import pl.fishingwear.blog.service.BlogCategoryService;
import pl.fishingwear.blog.service.BlogService;

import java.util.List;


@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final BlogCategoryService blogCategoryService;
    private final PostRepository postRepository;

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



}
