package project.app.flutter_spring_todoapp.todo.repository;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.notification.repository.NotificationRepository;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.dto.response.DetailTodoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @DisplayName("현재 유저의 할일들을 조회한다.")
    @Test
    void findAllByMemberIdOrderByIdDesc(){
        //given
        String email = "email@email.com";
        String nickName = "닉네임";
        String profile = "profile";
        Member member = Member.builder()
                .nickName(nickName)
                .role(Role.USER)
                .email(email)
                .profile(profile)
                .build();
        memberRepository.save(member);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
                ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        todoRepository.saveAll(List.of(
                createTodo(member, startDate, dueDate,1),
                createTodo(member, startDate, dueDate,2),
                createTodo(member, startDate, dueDate,3)
        ));
        //when
        List<Todo> result = todoRepository.findAllByMemberIdOrderByIdDesc(member.getId());
        //then
        Assertions.assertThat(result).hasSize(3)
                .extracting("title","description")
                .containsExactly(
                        tuple("할일 이름3","할일 설명3"),
                        tuple("할일 이름2","할일 설명2"),
                        tuple("할일 이름1","할일 설명1")
                        );
    }

    @DisplayName("Todo와 notification의 timeType을 Dto로 조회한다.")
    @Test
    void findTodoDetailByTodoId(){
        //given
        String email = "email@email.com";
        String nickName = "닉네임";
        String profile = "profile";
        Member member = Member.builder()
                .nickName(nickName)
                .role(Role.USER)
                .email(email)
                .profile(profile)
                .build();
        memberRepository.save(member);

        LocalDateTime startDate = LocalDateTime.of(2025,3,1
                ,11,0,0,0);
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
                ,startDate.getHour(),startDate.getMinute(),0,0);

        Todo todo = createTodo(member, startDate, dueDate, 1);
        todoRepository.save(todo);

        Notification notification =Notification.builder()
                .title(todo.getTitle())
                .content(todo.getDescription())
                .member(member)
                .todo(todo)
                .dueTime(dueDate)
                .timeType(TimeType.HALF)
                .build();
        notificationRepository.save(notification);
        //when
        DetailTodoResponse result = todoRepository.findTodoDetailByTodoId(todo.getId()).get();
        //then
        Assertions.assertThat(result).isNotNull()
                .extracting(
                        "title", "description",
                        "startDate", "dueDate", "status",
                        "priority","timeType")
                .containsExactlyInAnyOrder(todo.getTitle(),todo.getDescription(),
                        todo.getStartDate(),
                        todo.getDueDate(),
                        todo.getStatus(),
                        todo.getPriority(),notification.getTimeType());

    }

    @DisplayName("Todo와 notification의 timeType을 Dto로 조회한다.")
    @Test
    void findTodoDetailByTodoIdWithNotExistNotification(){
        //given
        String email = "email@email.com";
        String nickName = "닉네임";
        String profile = "profile";
        Member member = Member.builder()
                .nickName(nickName)
                .role(Role.USER)
                .email(email)
                .profile(profile)
                .build();
        memberRepository.save(member);

        LocalDateTime startDate = LocalDateTime.of(2025,3,1
                ,11,0,0,0);
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
                ,startDate.getHour(),startDate.getMinute(),0,0);

        Todo todo = createTodo(member, startDate, dueDate, 1);
        todoRepository.save(todo);

        //when
        DetailTodoResponse result = todoRepository.findTodoDetailByTodoId(todo.getId()).get();
        //then
        Assertions.assertThat(result).isNotNull()
                .extracting(
                        "title", "description",
                        "startDate", "dueDate", "status",
                        "priority","timeType")
                .containsExactlyInAnyOrder(todo.getTitle(),todo.getDescription(),
                        todo.getStartDate(),
                        todo.getDueDate(),
                        todo.getStatus(),
                        todo.getPriority(),TimeType.NONE);

    }

    private static Todo createTodo(final Member member, final LocalDateTime startDate, final LocalDateTime dueDate,final int num) {
        return Todo.builder()
                .title("할일 이름" + num)
                .member(member)
                .status(TodoStatus.IN_PROGRESS)
                .startDate(startDate)
                .dueDate(dueDate)
                .description("할일 설명" + num)
                .priority(TodoPriority.MEDIUM)
                .build();
    }

}