package my.learn.mireaffjpractice4.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTaskRequest {

    private String title;

}
