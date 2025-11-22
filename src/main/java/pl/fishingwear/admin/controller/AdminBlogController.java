package pl.fishingwear.admin.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.fishingwear.admin.dto.AdminCommentDto;
import pl.fishingwear.admin.dto.BlogCategoryDto;
import pl.fishingwear.admin.service.AdminCategoryService;
import pl.fishingwear.admin.service.AdminCommentService;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminBlogController {
    private final AdminCommentService adminCommentService;
    private final AdminCategoryService categoryService;


    @GetMapping("/blog")
    public String blogManage(Model model) {
        List<BlogCategoryDto> categories = categoryService.getAllCategories();
        List<AdminCommentDto> pendingComments = adminCommentService.getPendingComments();
        model.addAttribute("pendingComments", pendingComments);
        model.addAttribute("pendingCommentCount", pendingComments.size());
        model.addAttribute("categories", categories);


        return "admin/blog-manage";
    }

    @PostMapping("/blog/comments/accept/{id}")
    public String acceptComment(@PathVariable("id") Long commentId, RedirectAttributes redirectAttributes) {
        try {
            adminCommentService.acceptComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Komentarz został pomyślnie zaakceptowany i opublikowany.");

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: Komentarz nie został znaleziony.");
        }

        return "redirect:/admin/blog#comments";
    }

    @PostMapping("/blog/comments/reject/{id}")
    public String rejectComment(@PathVariable("id") Long commentId, RedirectAttributes redirectAttributes) {
        try {
            adminCommentService.rejectComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Komentarz został pomyślnie odrzucony i usunięty z listy oczekujących.");

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: Komentarz nie został znaleziony.");
        }

        return "redirect:/admin/blog#comments";
    }
}
