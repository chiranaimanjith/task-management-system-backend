package edu.icet.crm.service.admin;

import edu.icet.crm.entity.Comment;
import edu.icet.crm.entity.Task;
import edu.icet.crm.entity.User;
import edu.icet.crm.enums.TaskStatus;
import edu.icet.crm.enums.UserRole;
import edu.icet.crm.model.CommentDTO;
import edu.icet.crm.model.TaskDTO;
import edu.icet.crm.model.UserDto;
import edu.icet.crm.repository.CommentRepository;
import edu.icet.crm.repository.TaskRepository;
import edu.icet.crm.repository.UserRepository;
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
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;
    private final CommentRepository commentRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if (optionalUser.isPresent()) {
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setDueDate(taskDTO.getDueDate());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            return taskRepository.save(task).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllTask() {
        return taskRepository.findAll().stream().sorted(Comparator.comparing(Task::getDueDate).reversed()).map(Task::getTaskDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if (optionalTask.isPresent() && optionalUser.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());
            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus())));
            existingTask.setUser(optionalUser.get());
            return taskRepository.save(existingTask).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> searchTaskByTitle(String title) {
        return taskRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
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

    private TaskStatus mapStringToTaskStatus(String status) {
        return switch (status) {
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }
}
