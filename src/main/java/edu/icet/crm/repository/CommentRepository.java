package edu.icet.crm.repository;

import edu.icet.crm.entity.Comment;
import edu.icet.crm.model.CommentDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskId(Long taskId);
}
