package my.learn.mireaffjpractice4.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.learn.mireaffjpractice4.dto.request.CreateTaskRequest;
import my.learn.mireaffjpractice4.dto.request.UpdateTaskRequest;
import my.learn.mireaffjpractice4.dto.responce.TaskDTO;
import my.learn.mireaffjpractice4.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer limit,
                                   @RequestParam(required = false) Boolean done) {

        return new ResponseEntity<>(taskService.getTasks(page, limit, done),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid CreateTaskRequest createTaskRequest) {
        return new ResponseEntity<>(
                taskService.createAndSaveTask(createTaskRequest),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(
                taskService.findTaskById(id),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTaskById(@PathVariable(name = "id") Long id,
                                                  @RequestBody UpdateTaskRequest body) {
        return new ResponseEntity<>(
                taskService.updateTask(body, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTaskById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(
                taskService.deleteTask(id),
                HttpStatus.NO_CONTENT
        );
    }
























}
