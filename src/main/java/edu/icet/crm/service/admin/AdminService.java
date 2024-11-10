package edu.icet.crm.service.admin;

import edu.icet.crm.model.TaskDTO;
import edu.icet.crm.model.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getUsers();
    TaskDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getAllTask();

}
