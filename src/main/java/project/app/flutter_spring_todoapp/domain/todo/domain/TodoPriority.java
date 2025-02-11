package project.app.flutter_spring_todoapp.domain.todo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoPriority {

    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    final String name;
}
