package project.app.flutter_spring_todoapp.notification.repository;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.assertj.core.api.Assertions;
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
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("할일의 id를 참조하고 있는 알림을 단건 조회한다.")
    @Test
    void findNotificationByTodoId(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();

        memberRepository.save(member);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
                ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        Todo todo = Todo.builder()
                .title("할일 제목")
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.IN_PROGRESS)
                .priority(TodoPriority.MEDIUM)
                .member(member)
                .build();

        todoRepository.save(todo);

        Notification notification = Notification.builder()
                .title("할일이 30분 정도 남았습니다.")
                .dueTime(dueDate)
                .timeType(TimeType.HALF)
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .todo(todo)
                .member(member)
                .build();

        notificationRepository.save(notification);

        //when
        Notification result = notificationRepository.findNotificationByTodoId(todo.getId()).get();
        //then

        assertThat(result).isNotNull()
                .extracting("title", "content", "timeType", "isRead", "dueTime")
                .containsExactly(notification.getTitle(),notification.getContent(),
                        notification.getTimeType(),false,notification.getDueTime());
    }


    @DisplayName("할일의 id를 참조하고 있는 알림이 존재 여부를 조회한다.")
    @Test
    void existsNotificationByTodoId(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();

        memberRepository.save(member);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
                ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        Todo todo = Todo.builder()
                .title("할일 제목")
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.IN_PROGRESS)
                .priority(TodoPriority.MEDIUM)
                .member(member)
                .build();

        todoRepository.save(todo);

        Notification notification = Notification.builder()
                .title("할일이 30분 정도 남았습니다.")
                .dueTime(dueDate)
                .timeType(TimeType.HALF)
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .todo(todo)
                .member(member)
                .build();

        notificationRepository.save(notification);

        //when
        boolean result = notificationRepository.existsNotificationByTodoId(todo.getId());
        //then

        assertThat(result).isTrue();
    }

    @DisplayName("할일의 id를 참조하고 있는 알림이 존재 여부를 조회한다.")
    @Test
    void deleteAllByTodoId(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();

        memberRepository.save(member);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
                ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        Todo todo = Todo.builder()
                .title("할일 제목")
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.IN_PROGRESS)
                .priority(TodoPriority.MEDIUM)
                .member(member)
                .build();

        todoRepository.save(todo);

        Notification notification = Notification.builder()
                .title("할일이 30분 정도 남았습니다.")
                .dueTime(dueDate)
                .timeType(TimeType.HALF)
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .todo(todo)
                .member(member)
                .build();

        notificationRepository.save(notification);

        //when
        notificationRepository.deleteAllByTodoId(todo.getId());
        List<Notification> result = notificationRepository.findAll();
        //then

        assertThat(result).isEmpty();
    }
}