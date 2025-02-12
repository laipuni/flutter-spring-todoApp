package project.app.flutter_spring_todoapp.web.converter;

import org.springframework.core.convert.converter.Converter;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;


public class TodoStatusToStringConverter implements Converter<TodoStatus,String> {
    @Override
    public String convert(final TodoStatus source) {
        return source.getName();
    }
}
