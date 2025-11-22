package pl.fishingwear.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.admin.dto.AdminCommentDto;
import pl.fishingwear.admin.mapper.CommentMapper;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.enums.CommentStatus;
import pl.fishingwear.blog.repository.CommentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AdminCommentService {


    private final CommentRepository commentRepository;

    public List<AdminCommentDto> getPendingComments() {
        return commentRepository.findByStatus(CommentStatus.PENDING)
                .stream()
                .map(CommentMapper::toDto).toList();
    }

    @Transactional // Zapewnia transakcyjność operacji w bazie danych
    public void acceptComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Komentarz o ID " + commentId + " nie został znaleziony."));

        // Logika zmiany statusu
        comment.setStatus(CommentStatus.APPROVED);
        // commentRepository.save(comment); // Opcjonalne, jeśli nie masz zintegrowanego Session Flush

    }

    @Transactional
    public void rejectComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Komentarz o ID " + commentId + " nie został znaleziony."));

        comment.setStatus(CommentStatus.REJECTED);
    }


}
