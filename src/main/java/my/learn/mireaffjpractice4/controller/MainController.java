package my.learn.mireaffjpractice4.controller;

import my.learn.mireaffjpractice4.dto.responce.StatusDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1"})
public class MainController {

    @GetMapping("/health")
    public ResponseEntity<StatusDTO> getHealth() {
        StatusDTO health = StatusDTO
                .builder()
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(health, HttpStatus.OK);
    }

    @GetMapping("/favicon.ico")
    public void getFavicon() {
    }

}
