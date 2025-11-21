package pl.fishingwear.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.PostStatus;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {


//    @Query(value = "SELECT * FROM blog_post p " +
//            "WHERE (LOWER(p.title) LIKE CONCAT('%', :search, '%') " +
//            "OR LOWER(p.content) LIKE CONCAT('%', :search, '%')) " +
//            // Dodatkowy warunek filtrowania po kategorii (jeśli :categoryId jest podane)
//            "AND (p.category_id = :categoryId OR :categoryId IS NULL) " +
//            "ORDER BY p.created_at DESC",
//            nativeQuery = true)
//    Page<Post> searchPosts(
//            @Param("search") String search,
//            @Param("categoryId") Long categoryId,
//            Pageable pageable);
@Query(value = "SELECT * FROM blog_post p " +
        "WHERE p.status = 'PUBLISHED' " +
        "AND (" +
        "   LOWER(p.title) LIKE CONCAT('%', :search, '%') " +
        "   OR LOWER(p.content) LIKE CONCAT('%', :search, '%')" +
        ") " +
        "AND (p.category_id = :categoryId OR :categoryId IS NULL) " +
        "ORDER BY p.created_at DESC",

        countQuery = "SELECT count(*) FROM blog_post p " +
                "WHERE p.status = 'PUBLISHED' " +
                "AND (" +
                "   LOWER(p.title) LIKE CONCAT('%', :search, '%') " +
                "   OR LOWER(p.content) LIKE CONCAT('%', :search, '%')" +
                ") " +
                "AND (p.category_id = :categoryId OR :categoryId IS NULL)",
        nativeQuery = true)
Page<Post> searchPosts(
        @Param("search") String search,
        @Param("categoryId") Long categoryId,
        Pageable pageable);

    long countByStatus(PostStatus status);
    List<Post> findTop3ByStatusOrderByCreatedAtDesc(PostStatus status);
    Optional<Post> findByIdAndStatus(Long id, PostStatus status);
    Optional<Post> findById(Long id);
}
