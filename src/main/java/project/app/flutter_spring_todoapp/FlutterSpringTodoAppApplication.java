package project.app.flutter_spring_todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FlutterSpringTodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlutterSpringTodoAppApplication.class, args);
    }

}
