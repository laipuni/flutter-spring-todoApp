package project.app.flutter_spring_todoapp.todo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.todo.TodoUpdateDto;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.dto.TodoSaveDto;
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

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("추가할 할일의 요청값들을 받아 todo를 생성하고 결과를 반환한다.")
    @Test
    void save(){
        //given
        Member member = Member.builder()
                .nickName("nickName")
                .profile("profile")
                .email("email")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();

        memberRepository.save(member);

        String title = "제목";
        String description = "할일 설명";
        LocalDateTime now = LocalDateTime.now();
        //테스트 시각부터 1년뒤로 설정
        LocalDateTime dueDate = LocalDateTime.of(now.getYear() + 1,now.getMonth(),now.getDayOfMonth()
                ,now.getHour(),now.getMinute());
        TodoStatus status = TodoStatus.TODO;
        TodoPriority priority = TodoPriority.MEDIUM;

        TodoSaveDto request = TodoSaveDto.builder()
                .title(title)
                .description(description)
                .startDate(now)
                .dueDate(dueDate)
                .status(status)
                .priority(priority)
                .build();
        //when
        Todo response = todoService.save(request, member.getId());
        //then
        Assertions.assertThat(response).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(title,description,now,dueDate,status,priority);
    }

    @DisplayName("수정할 할일의 요청값들을 받아 수정하고 결과를 반환한다.")
    @Test
    void update(){
        //given
        Member member = Member.builder()
                .nickName("nickName")
                .profile("profile")
                .email("email")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();
        memberRepository.save(member);

        LocalDateTime startDate = LocalDateTime.now();
        // dueDate는 테스트 시각 1년 뒤로 설정
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),
                startDate.getDayOfMonth(), startDate.getHour(), startDate.getMinute());
        TodoStatus status = TodoStatus.TODO;
        TodoPriority priority = TodoPriority.MEDIUM;
        Todo todo = Todo.builder()
                .member(member)
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
        TodoStatus updatedStatus = TodoStatus.IN_PROGRESS;
        TodoPriority updatedPriority = TodoPriority.HIGH;

        TodoUpdateDto request = TodoUpdateDto.builder()
                .todoId(todo.getId())
                .title(updatedTitle)
                .description(updatedDescription)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(updatedStatus)
                .priority(updatedPriority)
                .updaterId(member.getId())
                .build();
        //when
        Todo response = todoService.update(request);
        //then
        Assertions.assertThat(response).isNotNull()
                .extracting("title","description","startDate","dueDate","status","priority")
                .containsExactly(updatedTitle,updatedDescription,request.getStartDate(),request.getDueDate(),updatedStatus,updatedPriority);
    }
}