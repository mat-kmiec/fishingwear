package pl.fishingwear.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.blog.exception.CommentNotFoundException;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.CommentVote;
import pl.fishingwear.blog.repository.CommentRepository;
import pl.fishingwear.blog.repository.CommentVoteRepository;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentVoteService {

    private final CommentRepository commentRepository;
    private final CommentVoteRepository voteRepository;
    private final UserRepository userRepository;

    @Transactional
    public int vote(Long commentId, Long userId, boolean upvote) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (comment.getAuthor().getId().equals(userId)) {
            throw new IllegalStateException("Nie możesz głosować na swój własny komentarz.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        User author = comment.getAuthor();

        CommentVote existingVote =
                voteRepository.findByCommentIdAndUserId(commentId, userId).orElse(null);

        if (existingVote == null) {
            CommentVote newVote = CommentVote.builder()
                    .comment(comment)
                    .user(user)
                    .isUpvote(upvote)
                    .build();

            voteRepository.save(newVote);

            updateReputation(author, upvote ? +1 : -1);

            return comment.getScore();
        }

        if (existingVote.isUpvote() == upvote) {
            return comment.getScore();
        }

        existingVote.setUpvote(upvote);
        voteRepository.save(existingVote);
        updateReputation(author, upvote ? +2 : -2);

        return comment.getScore();
    }

    private void updateReputation(User user, int delta) {
        int newRep = user.getReputationPoints() + delta;
        user.setReputationPoints(newRep);
        userRepository.save(user);
    }
}
