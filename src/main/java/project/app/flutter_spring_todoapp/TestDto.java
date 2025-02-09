package project.app.flutter_spring_todoapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class TestDto {

    private Long id;
    private String title;
    private String content;


}
