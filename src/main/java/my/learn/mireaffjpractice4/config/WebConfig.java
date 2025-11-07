package my.learn.mireaffjpractice4.config;

import lombok.RequiredArgsConstructor;
import my.learn.mireaffjpractice4.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor logger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Логирование для всех endpoints
        registry.addInterceptor(logger)
                .addPathPatterns("/**");

    }
}
