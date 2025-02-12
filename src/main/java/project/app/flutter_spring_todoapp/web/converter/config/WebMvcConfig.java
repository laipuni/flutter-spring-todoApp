package project.app.flutter_spring_todoapp.web.converter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.web.converter.*;

import java.time.LocalDate;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public Converter<TodoStatus, String> todoStatusToStringConverter(){
        return new TodoStatusToStringConverter();
    }

    public Converter<String, TodoStatus> stringToTodoStatusConverter(){
        return new StringToTodoStatusConverter();
    }

    public Converter<TodoPriority, String> todoPriorityToStringConverter(){
        return new TodoPriorityToStringConverter();
    }

    public Converter<String, TodoPriority> stringToTodoPriorityConverter(){
        return new StringToTodoPriorityConverter();
    }


    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(todoStatusToStringConverter());
        registry.addConverter(stringToTodoStatusConverter());
        registry.addConverter(todoPriorityToStringConverter());
        registry.addConverter(stringToTodoPriorityConverter());
    }
}
