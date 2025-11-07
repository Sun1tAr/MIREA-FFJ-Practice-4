package my.learn.mireaffjpractice4.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Task implements Comparable<Task>{

    private Long id;
    private String title;
    private Boolean done;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public int compareTo(Task t) {
        return getId().compareTo(t.getId());
    }
}
