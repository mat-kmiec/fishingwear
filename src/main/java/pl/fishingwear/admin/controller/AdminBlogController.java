package pl.fishingwear.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.fishingwear.admin.dto.blog.AdminCommentDto;
import pl.fishingwear.admin.dto.blog.BlogCategoryDto;
import pl.fishingwear.admin.dto.user.StaffUserDto;
import pl.fishingwear.admin.dto.blog.BlogCategoryCreationDto;
import pl.fishingwear.admin.exception.CategoryNotFoundException;
import pl.fishingwear.admin.service.BlogManagementService;
import pl.fishingwear.admin.service.UserManagementService;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminBlogController {
    private final BlogManagementService blogManagementService;
    private final UserManagementService userManagementService;


    @GetMapping("/blog")
    public String blogManage(Model model) {
        List<BlogCategoryDto> categories = blogManagementService.getAllCategories();
        List<AdminCommentDto> pendingComments = blogManagementService.getPendingComments();
        List<StaffUserDto> staffUsers = userManagementService.getAllStaff();
        model.addAttribute("pendingComments", pendingComments);
        model.addAttribute("pendingCommentCount", pendingComments.size());
        model.addAttribute("categories", categories);
        model.addAttribute("staffUsers", staffUsers);
        model.addAttribute("categoryCreationDto", new BlogCategoryCreationDto(null, null, null));


        return "admin/blog-manage";
    }

    @PostMapping("/blog/comments/accept/{id}")
    public String acceptComment(@PathVariable("id") Long commentId, RedirectAttributes redirectAttributes) {
        try {
            blogManagementService.acceptComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Komentarz został pomyślnie zaakceptowany i opublikowany.");

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: Komentarz nie został znaleziony.");
        }

        return "redirect:/admin/blog#comments";
    }

    @PostMapping("/blog/comments/reject/{id}")
    public String rejectComment(@PathVariable("id") Long commentId, RedirectAttributes redirectAttributes) {
        try {
            blogManagementService.rejectComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Komentarz został pomyślnie odrzucony i usunięty z listy oczekujących.");

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: Komentarz nie został znaleziony.");
        }

        return "redirect:/admin/blog#comments";
    }

    @PostMapping("/blog/categories/create")
    public String createCategory(@ModelAttribute BlogCategoryCreationDto dto, RedirectAttributes redirectAttributes) {
        try {
            blogManagementService.createCategory(dto);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Kategoria '" + dto.name() + "' została pomyślnie utworzona.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Błąd: Nazwa kategorii jest nieprawidłowa. " + e.getMessage());

        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Błąd: Wybrana kategoria nadrzędna");

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Błąd uprawnień: " + e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Wystąpił nieoczekiwany błąd podczas tworzenia kategorii.");
        }

        return "redirect:/admin/blog#categories";
    }

    @PostMapping("/blog/categories/delete")
    public String deleteCategory(@RequestParam("id") Long categoryId,
                                 RedirectAttributes redirectAttributes) {
        try {
            blogManagementService.deleteCategory(categoryId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Kategoria o ID " + categoryId + " została pomyślnie usunięta.");

        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Błąd: Kategoria, którą próbujesz usunąć, nie została znaleziona.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Wystąpił nieoczekiwany błąd podczas usuwania kategorii.");
        }

        return "redirect:/admin/blog#categories";
    }
}
