package pl.fishingwear.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.admin.dto.blog.AdminCommentDto;
import pl.fishingwear.admin.dto.blog.BlogCategoryDto;
import pl.fishingwear.admin.dto.blog.BlogCategoryCreationDto;
import pl.fishingwear.admin.exception.CategoryNotFoundException;
import pl.fishingwear.admin.exception.DeletionConstraintException;
import pl.fishingwear.admin.mapper.blog.BlogCategoryMapper;
import pl.fishingwear.admin.mapper.blog.CommentMapper;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.enums.CommentStatus;
import pl.fishingwear.blog.repository.BlogCategoryRepository;
import pl.fishingwear.blog.repository.CommentRepository;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class BlogManagementService {


    private final BlogCategoryRepository blogCategoryRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<BlogCategoryDto> getAllCategories() {
        return blogCategoryRepository.findAll().stream().map(BlogCategoryMapper::toDto).toList();
    }

    public List<AdminCommentDto> getPendingComments() {
        return commentRepository.findByStatus(CommentStatus.PENDING)
                .stream()
                .map(CommentMapper::toDto).toList();
    }

    @Transactional
    public void acceptComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Komentarz o ID " + commentId + " nie został znaleziony."));
        comment.setStatus(CommentStatus.APPROVED);

    }

    @Transactional
    public void rejectComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Komentarz o ID " + commentId + " nie został znaleziony."));

        comment.setStatus(CommentStatus.REJECTED);
    }

    @Transactional
    public void createCategory(BlogCategoryCreationDto dto){

        if(dto.name() == null || dto.name().isBlank()){
            throw new IllegalArgumentException("Nazwa kategorii nie może być pusta.");
        }

        User assignedModerator = dto.moderatorId() != 0
                ? userRepository.findById(dto.moderatorId()).orElseThrow(UserNotFoundException::new)
                : null;

        BlogCategory parentCategory = dto.parentCategoryId() != 0
                ? blogCategoryRepository.findById(dto.parentCategoryId()).orElseThrow(CategoryNotFoundException::new)
                : null;

        BlogCategory newCategory = BlogCategory.builder()
                .name(dto.name())
                .parent(parentCategory)
                .assignedModerator(assignedModerator)
                .build();

        blogCategoryRepository.save(newCategory);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!blogCategoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException();
        }
        blogCategoryRepository.deleteById(categoryId);
    }
}
