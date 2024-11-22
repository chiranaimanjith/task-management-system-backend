package edu.icet.crm.repository;

import edu.icet.crm.entity.Task;
import edu.icet.crm.model.TaskDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findAllByTitleContaining(String title);
}
