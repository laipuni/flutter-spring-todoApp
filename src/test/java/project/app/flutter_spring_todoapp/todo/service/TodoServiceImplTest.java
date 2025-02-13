package project.app.flutter_spring_todoapp.todo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.AddTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.UpdateTodoResponse;
import project.app.flutter_spring_todoapp.todo.repository.TodoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TodoServiceImplTest {

    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @DisplayName("추가할 할일의 요청값들을 받아 todo를 생성하고 결과를 반환한다.")
    @Test
    void save(){
        //given
        String title = "제목";
        String description = "할일 설명";
        LocalDateTime now = LocalDateTime.now();
        //테스트 시각부터 1년뒤로 설정
        LocalDateTime dueDate = LocalDateTime.of(now.getYear() + 1,now.getMonth(),now.getDayOfMonth()
                ,now.getHour(),now.getMinute());
        TodoStatus status = TodoStatus.TODO;
        TodoPriority priority = TodoPriority.MEDIUM;

        AddTodoRequest request = AddTodoRequest.builder()
                .title(title)
                .description(description)
                .startDate(now)
                .duetDate(dueDate)
                .status(status)
                .priority(priority)
                .build();
        //when
        AddTodoResponse response = todoService.save(request);
        //then
        Assertions.assertThat(response).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(title,description,now,dueDate,status,priority);
    }

    @DisplayName("수정할 할일의 요청값들을 받아 수정하고 결과를 반환한다.")
    @Test
    void update(){
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),
                startDate.getDayOfMonth(), startDate.getHour(), startDate.getMinute());
        TodoStatus status = TodoStatus.TODO;
        TodoPriority priority = TodoPriority.MEDIUM;

        Todo todo = Todo.builder()
                .title("제목")
                .description("설명")
                .startDate(startDate)
                .dueDate(dueDate)
                .status(status)
                .priority(priority)
                .build();

        todoRepository.save(todo);

        String updatedTitle = "수정된 제목";
        String updatedDescription = "수정된 할일 설명";
        LocalDateTime updatedStartDate = LocalDateTime.now();
        LocalDateTime updatedDueDate = LocalDateTime.of(updatedStartDate.getYear() + 1,updatedStartDate.getMonth()
                ,updatedStartDate.getDayOfMonth(),updatedStartDate.getHour(),updatedStartDate.getMinute());

        TodoStatus updatedStatus = TodoStatus.IN_PROGRESS;
        TodoPriority updatedPriority = TodoPriority.HIGH;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todo.getId())
                .title(updatedTitle)
                .description(updatedDescription)
                .startDate(updatedStartDate)
                .dueDate(updatedDueDate)
                .status(updatedStatus)
                .priority(updatedPriority)
                .build();
        //when
        UpdateTodoResponse response = todoService.update(request);
        //then
        Assertions.assertThat(response).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(updatedTitle,updatedDescription,updatedStartDate,updatedDueDate,updatedStatus,updatedPriority);
    }
}