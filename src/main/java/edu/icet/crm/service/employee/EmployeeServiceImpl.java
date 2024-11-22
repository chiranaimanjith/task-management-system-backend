package edu.icet.crm.service.employee;

import edu.icet.crm.entity.Task;
import edu.icet.crm.entity.User;
import edu.icet.crm.model.TaskDTO;
import edu.icet.crm.repository.TaskRepository;
import edu.icet.crm.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;

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
}
