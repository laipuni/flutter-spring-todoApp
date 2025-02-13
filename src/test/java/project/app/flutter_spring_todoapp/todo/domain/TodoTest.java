package project.app.flutter_spring_todoapp.todo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @DisplayName("status가 null로 Todo 생성할 때, 생성시각이 시작 시간과 마감 시간에 해당할 경우 In-progress상태이다. ")
    @Test
    void newTodoWithNullStatusAndBetweenStartAndDue(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //테스트 시각보다 1년 전
        LocalDateTime start = LocalDateTime.of(
                LocalDate.of(now.getYear() - 1,now.getMonth(),now.getDayOfMonth()),
                LocalTime.of(now.getHour(), now.getMinute(), now.getSecond())
        );
        //테스트 시각보다 1년 뒤
        LocalDateTime due = LocalDateTime.of(
                LocalDate.of(now.getYear() + 1,now.getMonth(),now.getDayOfMonth()),
                LocalTime.of(now.getHour(), now.getMinute(), now.getSecond())
        );
        //when
        String title = "제목";
        String description = "설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus expectedStatus = TodoStatus.IN_PROGRESS; // 테스트 예상 할일의 상태

        Todo todo = Todo.of(title, description,start,due, priority);

        //then
        assertThat(todo).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(title,description,start,due,expectedStatus,priority);
    }

    @DisplayName("status가 null로 Todo 생성할 때, 생성시각이 시작 시간보다 이를 경우 Todo상태이다. ")
    @Test
    void newTodoWithNullStatusAndBeforeStart(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //테스트 시각보다 1년 뒤
        LocalDateTime start = LocalDateTime.of(
                LocalDate.of(now.getYear() + 1,now.getMonth(),now.getDayOfMonth()),
                LocalTime.of(now.getHour(), now.getMinute(), now.getSecond())
        );
        //테스트 시각보다 2년 뒤
        LocalDateTime due = LocalDateTime.of(
                LocalDate.of(now.getYear() + 2,now.getMonth(),now.getDayOfMonth()),
                LocalTime.of(now.getHour(), now.getMinute(), now.getSecond())
        );
        //when
        String title = "제목";
        String description = "설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus expectedStatus = TodoStatus.TODO; // 테스트 예상 할일의 상태

        Todo todo = Todo.of(title, description,start,due, priority);

        //then
        assertThat(todo).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(title,description,start,due,expectedStatus,priority);
    }

    @DisplayName("status가 null로 Todo 생성할 때, 생성시각이 마감 시간보다 늦을 경우 Done상태이다. ")
    @Test
    void newTodoWithNullStatusAndAfterDue(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //테스트 시각보다 2년 전
        LocalDateTime start = LocalDateTime.of(
                LocalDate.of(now.getYear() - 2,now.getMonth(),now.getDayOfMonth()),
                LocalTime.of(now.getHour(), now.getMinute(), now.getSecond())
        );
        //테스트 시각보다 1년 전
        LocalDateTime due = LocalDateTime.of(
                LocalDate.of(now.getYear() - 1,now.getMonth(),now.getDayOfMonth()),
                LocalTime.of(now.getHour(), now.getMinute(), now.getSecond())
        );
        //when
        String title = "제목";
        String description = "설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus expectedStatus = TodoStatus.DONE; // 테스트 예상 할일의 상태

        Todo todo = Todo.of(title, description,start,due, priority);

        //then
        assertThat(todo).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(title,description,start,due,expectedStatus,priority);
    }

    @DisplayName("status가 null이 아닌 경우는 인자로 받은 상태로 Todo를 생성한다.")
    @Test
    void newTodo(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //테스트 시각보다 1년 후
        LocalDateTime due = LocalDateTime.of(
                LocalDate.of(now.getYear() + 1,now.getMonth(),now.getDayOfMonth()),
                LocalTime.of(now.getHour(), now.getMinute(), now.getSecond())
        );
        //when
        String title = "제목";
        String description = "설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus status = TodoStatus.TODO; // 테스트 예상 할일의 상태

        Todo todo = Todo.of(title, description,now,due,status,priority);

        //then
        assertThat(todo).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(title,description,now,due,status,priority);
    }
}