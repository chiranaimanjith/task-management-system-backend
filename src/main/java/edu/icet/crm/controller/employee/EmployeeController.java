package edu.icet.crm.controller.employee;

import edu.icet.crm.model.TaskDTO;
import edu.icet.crm.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(){
        return ResponseEntity.ok(employeeService.getTaskByUserId());
    }
}
