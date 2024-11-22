package edu.icet.crm.service.employee;

import edu.icet.crm.model.TaskDTO;

import java.util.List;

public interface EmployeeService {
    List<TaskDTO> getTaskByUserId();
}
