package my.learn.mireaffjpractice4.repository;

import my.learn.mireaffjpractice4.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    void saveTask(Task t);
    List<Task> getTasks();
    List<Task> getPaginatedTasks(Integer page, Integer limit);
    Optional<Task> findById(Long id);
    void updateTask(Task t);
    Boolean deleteTask(Task t);

}
