package project.app.flutter_spring_todoapp.web.converter;

import org.springframework.core.convert.converter.Converter;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.util.Optional;

public class StringToTodoPriorityConverter implements Converter<String, TodoPriority> {

    @Override
    public TodoPriority convert(final String source) {
        return Optional.ofNullable(TodoPriority.findPriority(source))
                .orElseThrow(() -> new IllegalArgumentException(source + "를 TodoStatus로 변환하지 못했습니다."));
    }
}
