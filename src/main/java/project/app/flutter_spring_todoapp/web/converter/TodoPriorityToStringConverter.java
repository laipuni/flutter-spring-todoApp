package project.app.flutter_spring_todoapp.web.converter;

import org.springframework.core.convert.converter.Converter;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

public class TodoPriorityToStringConverter implements Converter<TodoPriority,String> {
    @Override
    public String convert(final TodoPriority source) {
        return source.getName();
    }
}
