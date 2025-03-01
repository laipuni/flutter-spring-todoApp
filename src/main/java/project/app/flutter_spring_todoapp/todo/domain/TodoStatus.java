package project.app.flutter_spring_todoapp.todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum TodoStatus {

    TODO("todo"),
    IN_PROGRESS("in_progress"),
    DONE("done");

    private final String name;
    private final static Map<String,TodoStatus> statusMap = Arrays.stream(TodoStatus.values())
            .collect(Collectors.toMap(TodoStatus::getName,value -> value));

    @JsonCreator
    public static TodoStatus findStatus(final String source) {
        return statusMap.get(source.toLowerCase());
    }

    public String getName() {
        return name;
    }
}
