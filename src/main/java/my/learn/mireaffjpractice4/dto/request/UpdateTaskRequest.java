package my.learn.mireaffjpractice4.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotNull
    private Boolean done;
}
