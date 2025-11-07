package my.learn.mireaffjpractice4.repository.impl;

import my.learn.mireaffjpractice4.model.Task;
import my.learn.mireaffjpractice4.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskRepositoryImplTest {

    private final int TEST_LIST_SIZE = 90;

    private TaskRepository repository;

    private List<Task> testsTasks;



    @BeforeEach
    public void init() {
        Map<Long, Task> storage = new ConcurrentHashMap<>();
        AtomicLong idGenerator = new AtomicLong(0);
        repository = new InMemoryTaskRepositoryImpl(storage, idGenerator);
        testsTasks = new ArrayList<>();
        for (int i = 0; i < TEST_LIST_SIZE; i++) {
            testsTasks.add(Task.builder()
                    .title("Title_" + i)
                    .build());
        }
    }

    @Test
    public void trySaveAndGetTask_taskFind() {
        repository.saveTask(testsTasks.get(0));
        Optional<Task> task = repository.findById(testsTasks.get(0).getId());
        assertTrue(task.isPresent());
        assertNotNull(task.get());
        assertEquals(testsTasks.get(0), task.get());
    }

    @Test
    public void tryGetTask_taskNotFind() {
        Optional<Task> task = repository.findById(1L);
        assertTrue(task.isEmpty());
    }

    @Test
    public void trySaveAndGetTasksWithSortingById() {
        for (Task testsTask : testsTasks) {
            repository.saveTask(testsTask);
        }
        List<Task> tasks = repository.getTasks();
        assertEquals(testsTasks.size(), tasks.size());
        assertEquals(
                tasks.get(tasks.size()-1).getId(),
                tasks.get(0).getId() + tasks.size()-1
                );

    }

    @Test
    public void tryGetPaginatedTasks() {
        for (Task t: testsTasks) {
            repository.saveTask(t);
        }
        
        int page;
        int limit;
        List<Task> paginatedTasks;
        
        // Test 1
        paginatedTasks = getPaginatedTasks(0, TEST_LIST_SIZE + 2);
        assertEquals(testsTasks.size(), paginatedTasks.size());
        
        // Test 2
        paginatedTasks = getPaginatedTasks(2, TEST_LIST_SIZE);
        assertEquals(0, paginatedTasks.size());

        // Test 3
        page = 2;
        limit = TEST_LIST_SIZE/2;
        paginatedTasks = repository.getPaginatedTasks(page, limit);
        assertEquals(limit, paginatedTasks.size());
        assertEquals(limit + 1 ,paginatedTasks.get(0).getId());
    }

    @Test
    public void tryUpdateTask() {
        repository.saveTask(testsTasks.get(testsTasks.size()-1));
        Optional<Task> task = repository.findById(1L);
        assertTrue(task.isPresent());
        assertNotNull(task.get());
        Task gotTask = task.get();

        String newTitle = "URURU";

        gotTask.setTitle(newTitle);

        repository.updateTask(gotTask);

        task = repository.findById(1L);
        assertTrue(task.isPresent());
        assertNotNull(task.get());

        gotTask = task.get();
        assertEquals(newTitle, gotTask.getTitle());
    }


    @Test
    public void tryDeleteTask() {
        Task gotTask = testsTasks.get(testsTasks.size()-1);
        gotTask.setId(99L);
        assertFalse(repository.deleteTask(gotTask));
        repository.saveTask(gotTask);
        Optional<Task> task = repository.findById(1L);
        assertTrue(task.isPresent());
        assertNotNull(task.get());
        gotTask = task.get();

        assertTrue(repository.deleteTask(gotTask));
        assertFalse(repository.deleteTask(gotTask));
    }


    private List<Task> getPaginatedTasks(int page, int limit) {
        List<Task> paginatedTasks;
        paginatedTasks = repository.getPaginatedTasks(page, limit);
        return paginatedTasks;
    }

}