package pl.fishingwear.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.enums.CommentStatus;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndStatus(Long postId, CommentStatus status);

    List<Comment> findByPostId(Long id);

    @Query("""
    SELECT c FROM Comment c
    WHERE c.post.id = :postId
      AND (c.status = 'APPROVED' OR c.author.id = :userId)
    """)
    List<Comment> findVisibleForUser(Long postId, Long userId);

    long countCommentByStatusContains(CommentStatus status);

    long countByStatus(CommentStatus commentStatus);
    List<Comment> findByStatus(CommentStatus commentStatus);
}
