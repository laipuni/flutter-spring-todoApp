package project.app.flutter_spring_todoapp.todo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.notification.repository.NotificationRepository;
import project.app.flutter_spring_todoapp.redis.RedisService;
import project.app.flutter_spring_todoapp.security.oauth2.dto.SessionMember;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.DeleteTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.AddTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.DeleteTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.UpdateTodoResponse;
import project.app.flutter_spring_todoapp.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    MemberRepository memberRepository;

    @MockBean
    RedisService redisService;

    @DisplayName("예약할 때 할일과 알림을 같이 저장한다.")
    @Test
    void createTodoWithNotification(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();

        memberRepository.save(member);

        SessionMember sessionMember = SessionMember.builder()
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .profile(member.getProfile())
                .build();

        //테스트 시각 ~ 1년 뒤로 기간을 설정
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime due = LocalDateTime.of(start.getYear() + 1,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        AddTodoRequest request = AddTodoRequest.builder()
                .title("할일 제목")
                .description("할일 설명")
                .startDate(start)
                .dueDate(due)
                .priority(TodoPriority.MEDIUM)
                .status(TodoStatus.TODO)
                .timeType(TimeType.HALF)
                .build();
        //when
        AddTodoResponse response = reservationService.createTodoWithNotification(request, sessionMember);
        List<Notification> notificationList = notificationRepository.findAll();
        List<Todo> todoList = todoRepository.findAll();
        //then
        assertThat(response).isNotNull()
                .extracting("title","description", "startDate", "dueDate", "status", "priority", "timeType")
                .containsExactly(request.getTitle(),request.getDescription(),request.getStartDate(),request.getDueDate(),request.getStatus(),request.getPriority(),request.getTimeType());
        assertThat(notificationList).hasSize(1);
        assertThat(todoList).hasSize(1);
    }

    @DisplayName("예약할 때 시간을 설정하지 않았을 경우 할일만 저장한다.")
    @Test
    void createOnlyTodo(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();

        memberRepository.save(member);

        SessionMember sessionMember = SessionMember.builder()
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .profile(member.getProfile())
                .build();

        //테스트 시각 ~ 1년 뒤로 기간을 설정
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime due = LocalDateTime.of(start.getYear() + 1,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        AddTodoRequest request = AddTodoRequest.builder()
                .title("할일 제목")
                .description("할일 설명")
                .startDate(start)
                .dueDate(due)
                .priority(TodoPriority.MEDIUM)
                .status(TodoStatus.TODO)
                .timeType(null)
                .build();
        //when
        AddTodoResponse response = reservationService.createTodoWithNotification(request, sessionMember);
        List<Notification> notificationList = notificationRepository.findAll();
        List<Todo> todoList = todoRepository.findAll();
        //then
        assertThat(response).isNotNull()
                .extracting("title","description", "startDate", "dueDate", "status", "priority", "timeType")
                .containsExactly(request.getTitle(),request.getDescription(),request.getStartDate(),request.getDueDate(),request.getStatus(),request.getPriority(),TimeType.NONE);
        assertThat(notificationList).hasSize(0);
        assertThat(todoList).hasSize(1);
    }

    @DisplayName("알림의 시간 타입 혹은 마감시간이 변경되었을 때, 알림을 수정한다.")
    @Test
    void updateTodoWithNotification(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();
        memberRepository.save(member);

        SessionMember sessionMember = SessionMember.builder()
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .profile(member.getProfile())
                .build();

        //테스트 시각 ~ 1년 뒤로 기간을 설정
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime due = LocalDateTime.of(start.getYear() + 1,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        String title = "할일 제목";
        String description = "할일 설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus status = TodoStatus.TODO;
        Todo todo = Todo.builder()
                .title(title)
                .description(description)
                .startDate(start)
                .dueDate(due)
                .member(member)
                .priority(priority)
                .status(status)
                .build();
        todoRepository.save(todo);

        TimeType fifteen = TimeType.FIFTEEN;
        Notification notification = Notification.builder()
                .member(member)
                .todo(todo)
                .dueTime(due)
                .timeType(fifteen)
                .title("\"" + title + "\"이" + fifteen.getTime() + " 남았습니다.")
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .build();
        notificationRepository.save(notification);

        LocalDateTime editedStart = LocalDateTime.of(start.getYear() + 1,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        LocalDateTime editedDue = LocalDateTime.of(start.getYear() + 2,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todo.getId())
                .title("수정된 제목") // 할일 제목 수정
                .description("수정된 설명") // 설명란 수정
                .startDate(editedStart) // 현재 -> 1년 뒤부터 시작
                .dueDate(editedDue) // 1년 -> 2년
                .priority(TodoPriority.HIGH) // 중요도 중간 -> 높음
                .status(TodoStatus.IN_PROGRESS) // 해야할 일 -> 하는 중 상태 수정
                .timeType(TimeType.HALF) // 시간 타입을 15분 -> 30분 변경
                .build();
        //when
        UpdateTodoResponse response = reservationService.updateTodoWithNotification(request, sessionMember);
        List<Notification> notificationList = notificationRepository.findAll();
        List<Todo> todoList = todoRepository.findAll();

        //then
        assertThat(response).isNotNull()
                .extracting("title","description", "startDate", "dueDate", "status", "priority")
                .containsExactly(request.getTitle(),request.getDescription(),request.getStartDate(),request.getDueDate(),request.getStatus(),request.getPriority());
        assertThat(notificationList).hasSize(1);
        assertThat(todoList).hasSize(1);
    }

    @DisplayName("기존에 알림이 없던 할 일에 알림을 설정하면, 새로운 알림이 생성된다")
    @Test
    void updateTodoAndCreateNotification(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();
        memberRepository.save(member);

        SessionMember sessionMember = SessionMember.builder()
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .profile(member.getProfile())
                .build();

        //테스트 시각 ~ 1년 뒤로 기간을 설정
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime due = LocalDateTime.of(start.getYear() + 1,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        String title = "할일 제목";
        String description = "할일 설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus status = TodoStatus.TODO;
        Todo todo = Todo.builder()
                .title(title)
                .description(description)
                .startDate(start)
                .dueDate(due)
                .member(member)
                .priority(priority)
                .status(status)
                .build();
        todoRepository.save(todo);

        TimeType fifteen = TimeType.FIFTEEN;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todo.getId())
                .title(title)
                .description(description)
                .startDate(start)
                .dueDate(due)
                .priority(priority)
                .status(status)
                .timeType(TimeType.FIFTEEN) // 알림 설정
                .build();
        //when
        UpdateTodoResponse response = reservationService.updateTodoWithNotification(request, sessionMember);
        List<Notification> notificationList = notificationRepository.findAll();
        List<Todo> todoList = todoRepository.findAll();

        //then
        assertThat(response).isNotNull()
                .extracting("title","description", "startDate", "dueDate", "status", "priority")
                .containsExactly(request.getTitle(),request.getDescription(),request.getStartDate(),request.getDueDate(),request.getStatus(),request.getPriority());
        assertThat(notificationList).hasSize(1);
        assertThat(todoList).hasSize(1);
    }

    @DisplayName("예약한 할일을 수정하고, 알림을 제거한다.")
    @Test
    void updateTodoAndRemoveNotification(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();
        memberRepository.save(member);

        SessionMember sessionMember = SessionMember.builder()
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .profile(member.getProfile())
                .build();

        //테스트 시각 ~ 1년 뒤로 기간을 설정
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime due = LocalDateTime.of(start.getYear() + 1,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        String title = "할일 제목";
        String description = "할일 설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus status = TodoStatus.TODO;
        Todo todo = Todo.builder()
                .title(title)
                .description(description)
                .startDate(start)
                .dueDate(due)
                .member(member)
                .priority(priority)
                .status(status)
                .build();
        todoRepository.save(todo);

        TimeType fifteen = TimeType.FIFTEEN;
        Notification notification = Notification.builder()
                .member(member)
                .todo(todo)
                .dueTime(due)
                .timeType(fifteen)
                .title("\"" + title + "\"이" + fifteen.getTime() + " 남았습니다.")
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .build();
        notificationRepository.save(notification);

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todo.getId())
                .title(title)
                .description(description)
                .startDate(start)
                .dueDate(due)
                .priority(TodoPriority.HIGH)
                .status(TodoStatus.TODO)
                .timeType(TimeType.NONE) // 알림 설정 해제
                .build();
        //when
        UpdateTodoResponse response = reservationService.updateTodoWithNotification(request, sessionMember);
        List<Notification> notificationList = notificationRepository.findAll();
        List<Todo> todoList = todoRepository.findAll();

        //then
        assertThat(response).isNotNull()
                .extracting("title","description", "startDate", "dueDate", "status", "priority")
                .containsExactly(request.getTitle(),request.getDescription(),request.getStartDate(),request.getDueDate(),request.getStatus(),request.getPriority());
        assertThat(notificationList).hasSize(0);
        assertThat(todoList).hasSize(1);
    }

    @DisplayName("예약한 할일과 알림을 제거한다.")
    @Test
    void removeTodoWithNotification(){
        //given
        Member member = Member.builder()
                .nickName("nickname")
                .profile("profile")
                .email("email@email.com")
                .role(Role.USER)
                .fcmToken("fcmToken")
                .build();
        memberRepository.save(member);

        SessionMember sessionMember = SessionMember.builder()
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .profile(member.getProfile())
                .build();

        //테스트 시각 ~ 1년 뒤로 기간을 설정
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime due = LocalDateTime.of(start.getYear() + 1,start.getMonth(),start.getDayOfMonth(), start.getHour(),start.getMinute(),start.getSecond());
        String title = "할일 제목";
        String description = "할일 설명";
        TodoPriority priority = TodoPriority.MEDIUM;
        TodoStatus status = TodoStatus.TODO;
        Todo todo = Todo.builder()
                .title(title)
                .description(description)
                .startDate(start)
                .dueDate(due)
                .member(member)
                .priority(priority)
                .status(status)
                .build();
        todoRepository.save(todo);

        TimeType fifteen = TimeType.FIFTEEN;
        Notification notification = Notification.builder()
                .member(member)
                .todo(todo)
                .dueTime(due)
                .timeType(fifteen)
                .title("\"" + title + "\"이" + fifteen.getTime() + " 남았습니다.")
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .build();
        notificationRepository.save(notification);

        DeleteTodoRequest request = DeleteTodoRequest.builder()
                .todoId(todo.getId())
                .build();
        //when
        reservationService.removeTodoWithNotification(request, sessionMember);
        List<Notification> notificationList = notificationRepository.findAll();
        List<Todo> todoList = todoRepository.findAll();

        //then
        assertThat(notificationList).hasSize(0);
        assertThat(todoList).hasSize(0);
    }
}