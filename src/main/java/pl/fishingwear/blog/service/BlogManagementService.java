package pl.fishingwear.blog.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.fishingwear.common.exception.CategoryNotFoundException;
import pl.fishingwear.blog.dto.*;
import pl.fishingwear.blog.mapper.BlogCategoryMapper;
import pl.fishingwear.blog.mapper.CommentMapper;
import pl.fishingwear.blog.mapper.PostCreateMapper;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.CommentStatus;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.blog.repository.BlogCategoryRepository;
import pl.fishingwear.blog.repository.CommentRepository;
import pl.fishingwear.blog.repository.PostRepository;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.common.service.ImageService;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;
import pl.fishingwear.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class BlogManagementService {

    private final static String BLOG_UPLOAD_DIR = "uploads/blog/";

    private final BlogCategoryRepository blogCategoryRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostCreateMapper postCreateMapper;
    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;

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

    @Transactional
    public void updateCategory(CategoryEditDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID kategorii jest wymagane do edycji.");
        }

        BlogCategory category = blogCategoryRepository.findById(dto.getId())
                .orElseThrow(CategoryNotFoundException::new);

        category.setName(dto.getName());

        if (dto.getParentCategoryId() != null) {
            BlogCategory parent = blogCategoryRepository.findById(dto.getParentCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        if (dto.getModeratorId() != null || dto.getModeratorId() != 0) {
            User moderator = userRepository.findById(dto.getModeratorId())
                    .orElseThrow(UserNotFoundException::new);
            category.setAssignedModerator(moderator);
        } else {
            category.setAssignedModerator(null);
        }

        blogCategoryRepository.save(category);
    }

    @Transactional
    public void createPost(String title, String content, Long categoryId, MultipartFile image) { // usunąłem 'throws IOException'
        String fileName = null;

        if (image != null && !image.isEmpty()) {
            fileName = imageService.saveImage(
                    image,
                    BLOG_UPLOAD_DIR,
                    true,
                    1200, 630,
                    0.8,
                    0.9,
                    true
            );
        }

        User author = userService.getCurrentUser()
                .orElseThrow(UserNotFoundException::new);

        BlogCategory category = blogCategoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        PostCreateDto dto = new PostCreateDto(
                title,
                content,
                PostStatus.PUBLISHED,
                author,
                category
        );

        Post post = postCreateMapper.toEntity(dto, dto.getAuthor());
        post.setUpdatedAt(LocalDateTime.now());
        post.setImg(fileName);

        postRepository.save(post);
    }



}
