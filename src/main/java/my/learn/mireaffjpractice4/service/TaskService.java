package my.learn.mireaffjpractice4.service;

import my.learn.mireaffjpractice4.dto.request.CreateTaskRequest;
import my.learn.mireaffjpractice4.dto.request.UpdateTaskRequest;
import my.learn.mireaffjpractice4.dto.responce.TaskDTO;
import my.learn.mireaffjpractice4.model.Task;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public interface TaskService {

    List<TaskDTO> getTasks(Integer page, Integer limit, Boolean done);
    TaskDTO createAndSaveTask(CreateTaskRequest request);
    TaskDTO findTaskById(Long id);
    TaskDTO updateTask(UpdateTaskRequest request, Long id);
    TaskDTO deleteTask(Long id);





}
