package edu.icet.crm.service.employee;

import edu.icet.crm.entity.Comment;
import edu.icet.crm.entity.Task;
import edu.icet.crm.entity.User;
import edu.icet.crm.enums.TaskStatus;
import edu.icet.crm.model.CommentDTO;
import edu.icet.crm.model.TaskDTO;
import edu.icet.crm.repository.CommentRepository;
import edu.icet.crm.repository.TaskRepository;
import edu.icet.crm.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;
    private final CommentRepository commentRepository;

    @Override
    public List<TaskDTO> getTaskByUserId() {
        User user = jwtUtil.getLoggedInUser();
        if (user != null) {
            return taskRepository.findAllByUserId(user.getId())
                    .stream()
                    .sorted(Comparator.comparing(Task::getDueDate).reversed())
                    .map(Task::getTaskDTO).collect(Collectors.toList());
        }
        throw new EntityNotFoundException("User not found");
    }

    @Override
    public TaskDTO updateTask(Long id, String status) {
        Optional<Task> optionalTask=taskRepository.findById(id);
        if (optionalTask.isPresent()){
             Task existingTask= optionalTask.get();
             existingTask.setTaskStatus(mapStringToTaskStatus(status));
             return taskRepository.save(existingTask).getTaskDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public CommentDTO createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = jwtUtil.getLoggedInUser();
        if ((optionalTask.isPresent()) && user != null) {
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);
            return commentRepository.save(comment).getCommentDTO();
        }
        throw new EntityNotFoundException("User or Task not found");
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream().map(Comment::getCommentDTO).collect(Collectors.toList());
    }
    private TaskStatus mapStringToTaskStatus(String status){
        return switch (status){
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }
}
