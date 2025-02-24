package project.app.flutter_spring_todoapp.web.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.web.converter.*;
import project.app.flutter_spring_todoapp.web.resolver.SessionMemberArgumentResolver;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    HttpSession httpSession;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(sessionMemberArgumentResolver());
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(todoStatusToStringConverter());
        registry.addConverter(stringToTodoStatusConverter());
        registry.addConverter(todoPriorityToStringConverter());
        registry.addConverter(stringToTodoPriorityConverter());
    }

    public HandlerMethodArgumentResolver sessionMemberArgumentResolver(){
        return new SessionMemberArgumentResolver(httpSession);
    }

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
}
