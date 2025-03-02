package project.app.flutter_spring_todoapp.todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum TodoPriority {

    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    final String name;
    private final static Map<String,TodoPriority> priorityMap = Arrays.stream(TodoPriority.values())
            .collect(Collectors.toMap(TodoPriority::getName, value -> value));

    @JsonCreator
    public static TodoPriority findPriority(final String source) {
        return priorityMap.get(source.toLowerCase());
    }

    public String getName() {
        return name;
    }

}
