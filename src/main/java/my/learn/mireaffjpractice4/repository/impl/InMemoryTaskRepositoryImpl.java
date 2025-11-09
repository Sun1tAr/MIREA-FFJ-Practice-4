package my.learn.mireaffjpractice4.repository.impl;

import lombok.AllArgsConstructor;
import my.learn.mireaffjpractice4.model.Task;
import my.learn.mireaffjpractice4.repository.TaskRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@AllArgsConstructor
public class InMemoryTaskRepositoryImpl implements TaskRepository {

    private final Map<Long, Task> storage;
    private final AtomicLong idGenerator;

    public InMemoryTaskRepositoryImpl() {
        storage = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong(0);
    }

    @Override
    public void saveTask(@NotNull Task t) {
        t.setId(idGenerator.addAndGet(1));
        storage.put(t.getId(), t);
    }

    @Override
    public List<Task> getTasks() {
        return storage.values().stream()
                .sorted(Task::compareTo)
                .toList();
    }

    @Override
    public List<Task> getPaginatedTasks(Integer page, Integer limit) {
        List<Task> tasks = getTasks();
        return getPaginatedTasksFromList(page, limit, tasks);
    }

    @Override
    public Optional<Task> findById(Long id) {

        Task task = storage.get(id);
        if (task == null) {
            return Optional.empty();
        }
        return Optional.of(task);
    }

    @Override
    public void updateTask(Task t) {
        storage.put(t.getId(), t);
    }

    @Override
    public Boolean deleteTask(@NotNull Task t) {
        Task removed = storage.remove(t.getId());
        return removed != null;
    }

    @Override
    public List<Task> getDoneTasks(Boolean status) {
        return getTasks().stream()
                .filter(task -> task.getDone().equals(status))
                .toList();
    }

    @Override
    public List<Task> getPaginatedDoneTasks(Integer page, Integer limit, Boolean status) {
        return getPaginatedTasksFromList(page, limit, getDoneTasks(status));
    }

    private List<Task> getPaginatedTasksFromList(Integer page, Integer limit, List<Task> tasks) {
        if (limit > tasks.size()) {
            if (page > 1) {
                return List.of();
            } else {
                return tasks;
            }
        }
        int startIndex = (page - 1) * limit;

        if (startIndex >= tasks.size()) {
            return List.of();
        }
        int endIndex = startIndex + limit;
        if (endIndex > tasks.size()) {
            endIndex = tasks.size() - 1;
        }

        if (startIndex == endIndex) {
            return List.of(tasks.get(startIndex));
        }

        List<Task> paginatedTasks = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            paginatedTasks.add(tasks.get(i));
        }
        return paginatedTasks;
    }
}
