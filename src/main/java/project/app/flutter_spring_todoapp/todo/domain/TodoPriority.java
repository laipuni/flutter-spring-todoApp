package project.app.flutter_spring_todoapp.todo.domain;

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

    public static TodoPriority findPriority(final String source) {
        return priorityMap.get(source);
    }

    public String getName() {
        return name;
    }

}
