package my.learn.mireaffjpractice4.config;

import lombok.RequiredArgsConstructor;
import my.learn.mireaffjpractice4.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // для всех эндпоинтов
                .allowedOrigins("*")           // доступ есть с любых хостов
                .allowedMethods("*")           // для всех методов HTTP
                .allowedHeaders("*")           // в запросе можно ставить любые заголовки
                .maxAge(3600);                 // браузер может час не отправлять Options
    }
}
