package project.app.flutter_spring_todoapp.domain.todo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoStatus {

    TODO("todo"),
    IN_PROGRESS("in_progress"),
    DONE("done");

    final String name;
}
