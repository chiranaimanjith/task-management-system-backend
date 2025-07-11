package edu.icet.crm.service.admin;

import edu.icet.crm.model.CommentDTO;
import edu.icet.crm.model.TaskDTO;
import edu.icet.crm.model.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getUsers();
    TaskDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getAllTask();
    void deleteTask(Long id);
    TaskDTO updateTask(Long id, TaskDTO taskDTO);
    List<TaskDTO> searchTaskByTitle(String title);
    TaskDTO getTaskById(Long id);
    CommentDTO createComment(Long taskId, String content);
    List<CommentDTO> getCommentsByTaskId(Long taskId);
}
