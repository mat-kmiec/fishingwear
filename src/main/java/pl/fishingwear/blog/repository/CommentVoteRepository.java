package pl.fishingwear.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fishingwear.blog.model.CommentVote;

import java.util.Optional;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    Optional<CommentVote> findByCommentIdAndUserId(Long commentId, Long userId);
}
