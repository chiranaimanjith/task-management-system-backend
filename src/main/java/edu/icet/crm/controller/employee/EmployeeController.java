package edu.icet.crm.controller.employee;

import edu.icet.crm.model.TaskDTO;
import edu.icet.crm.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(){
        return ResponseEntity.ok(employeeService.getTaskByUserId());
    }

    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @PathVariable String status){
       TaskDTO updatedTaskDTO =employeeService.updateTask(id, status);
       if (updatedTaskDTO==null)
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
          return ResponseEntity.ok(updatedTaskDTO);
    }
}
