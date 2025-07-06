package edu.icet.crm.service.employee;

import edu.icet.crm.model.CommentDTO;
import edu.icet.crm.model.TaskDTO;

import java.util.List;

public interface EmployeeService {
    List<TaskDTO> getTaskByUserId();
    TaskDTO updateTask(Long id, String status);
    TaskDTO getTaskById(Long id);
    CommentDTO createComment(Long taskId, String content);
    List<CommentDTO> getCommentsByTaskId(Long taskId);
}
