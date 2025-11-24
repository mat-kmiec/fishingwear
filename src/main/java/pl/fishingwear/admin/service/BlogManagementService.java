package pl.fishingwear.admin.service;

import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.fishingwear.admin.dto.blog.*;
import pl.fishingwear.admin.exception.CategoryNotFoundException;
import pl.fishingwear.admin.mapper.blog.BlogCategoryMapper;
import pl.fishingwear.admin.mapper.blog.CommentMapper;
import pl.fishingwear.admin.mapper.blog.PostCreateMapper;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.CommentStatus;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.blog.repository.BlogCategoryRepository;
import pl.fishingwear.blog.repository.CommentRepository;
import pl.fishingwear.blog.repository.PostRepository;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;
import pl.fishingwear.user.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BlogManagementService {

    private final static String UPLOAD_DIR = "uploads/blog/";

    private final BlogCategoryRepository blogCategoryRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostCreateMapper postCreateMapper;
    private final PostRepository postRepository;
    private final UserService userService;

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
    public void createPost(String title, String content, Long categoryId, MultipartFile image) throws IOException {
        String fileName = null;
        if (image != null && !image.isEmpty()) {
            fileName = saveImage(image);
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

    public String saveImage(MultipartFile file) {
        try {
            String uuid = UUID.randomUUID().toString();
            String filename = uuid + ".jpg";
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            File mainFile = uploadPath.resolve(filename).toFile();
            File thumbFile = uploadPath.resolve("thumb_" + filename).toFile();
            Thumbnails.of(file.getInputStream())
                    .scale(1.0)
                    .outputQuality(0.9)
                    .toFile(mainFile);


            Thumbnails.of(mainFile)
                    .size(1200, 630)
                    .keepAspectRatio(true)
                    .outputQuality(0.8)
                    .toFile(thumbFile);

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas zapisu pliku: " + e.getMessage(), e);
        }
    }

}
