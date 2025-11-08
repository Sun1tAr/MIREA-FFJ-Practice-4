package my.learn.mireaffjpractice4.service.impl;

import lombok.RequiredArgsConstructor;
import my.learn.mireaffjpractice4.dto.request.CreateTaskRequest;
import my.learn.mireaffjpractice4.dto.request.UpdateTaskRequest;
import my.learn.mireaffjpractice4.dto.responce.TaskDTO;
import my.learn.mireaffjpractice4.exception.InvalidArgumentException;
import my.learn.mireaffjpractice4.exception.NotFoundException;
import my.learn.mireaffjpractice4.model.Task;
import my.learn.mireaffjpractice4.repository.TaskRepository;
import my.learn.mireaffjpractice4.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<TaskDTO> getTasks(Integer page, Integer limit, Boolean done) {
        boolean pagination;
        boolean doneFilter;

        doneFilter = done != null;

        if (page != null &&  limit != null) {
            if (page <= 0 ||  limit <= 0) {
                throw new InvalidArgumentException(
                        "page must be greater than 0 and limit must be greater than 0",
                        HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.toString()) {
                };
            } else {
                pagination = true;
            }
        } else {
            pagination = false;
        }

        List<Task> tasks;
        if (pagination && doneFilter) {
            tasks = taskRepository.getPaginatedDoneTasks(page, limit, done);
        } else if (pagination) {
            tasks = taskRepository.getPaginatedTasks(page, limit);
        } else if (doneFilter) {
            tasks = taskRepository.getDoneTasks(done);
        } else {
            tasks = taskRepository.getTasks();
        }

        return tasks.stream()
                .map(t -> mapTaskToDTO(t))
                .toList();
    }

    @Override
    public TaskDTO createAndSaveTask(CreateTaskRequest request) {
        Task t = Task.builder()
                .title(request.getTitle())
                .done(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        taskRepository.saveTask(t);
        return mapTaskToDTO(t);
    }

    @Override
    public TaskDTO findTaskById(Long id) {
        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException();
        }
        Task t = byId.get();
        return mapTaskToDTO(t);
    }

    @Override
    public TaskDTO updateTask(UpdateTaskRequest request, Long id) {
        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException();
        }
        Task t = byId.get();
        t.setTitle(request.getTitle());
        t.setDone(false);
        t.setUpdatedAt(LocalDateTime.now());
        taskRepository.updateTask(t);
        return mapTaskToDTO(t);
    }

    @Override
    public TaskDTO deleteTask(Long id) {
        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException();
        }
        Task task = byId.get();
        if (taskRepository.deleteTask(task)) {
            return mapTaskToDTO(task);
        } else {
            throw new NotFoundException();
        }
    }

    private TaskDTO mapTaskToDTO(Task t) {
        return TaskDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .done(t.getDone())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }












}
