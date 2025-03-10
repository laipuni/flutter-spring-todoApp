package project.app.flutter_spring_todoapp.notification.service;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Tuple;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.IntegrationTestSupport;
import project.app.flutter_spring_todoapp.exception.global.UnAuthorizationException;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.notification.dto.NotificationRemoveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationSaveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationUpdateDto;
import project.app.flutter_spring_todoapp.notification.repository.NotificationRepository;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@Transactional
class NotificationServiceTest extends IntegrationTestSupport {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationRepository notificationRepository;

    @DisplayName("예약할 할일과 알림의 정보를 받아 알림을 설정한다.")
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

        LocalDateTime startDate = LocalDateTime.now();
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

        NotificationSaveDto saveDto = NotificationSaveDto.builder()
                .todoId(todo.getId())
                .timeType(TimeType.HALF)
                .dueDate(dueDate)
                .build();

        //when
        notificationService.save(saveDto);
        List<Notification> result = notificationRepository.findAll();
        //then
        assertThat(result).hasSize(1)
                .extracting("timeType","isRead","dueTime")
                .containsExactlyInAnyOrder(tuple(saveDto.getTimeType(),false,dueDate));
    }

    @DisplayName("알림을 저장할 때, 알림을 설정할 할일이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void saveWithNotExistTodo(){
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
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),
                startDate.getDayOfMonth(), startDate.getHour(), startDate.getMinute());

        long invalidTodoId = 2025L;
        NotificationSaveDto saveDto = NotificationSaveDto.builder()
                .todoId(invalidTodoId)
                .timeType(TimeType.HALF)
                .dueDate(dueDate)
                .build();

        //when
        //then
        assertThatThrownBy(() -> notificationService.save(saveDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 할일은 존재하지 않습니다.");
    }

    @DisplayName("수정할 알림의 정보를 받아 알림을 수정한다.")
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

        TimeType fifteen = TimeType.FIFTEEN;
        Notification notification = Notification.builder()
                .member(member)
                .todo(todo)
                .dueTime(dueDate)
                .timeType(fifteen)
                .title(String.format("\"%s\"가 %s 남았습니다.", todo.getTitle(), fifteen.getDescription()))
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .build();
        notificationRepository.save(notification);

        String editedTitle = "수정된 제목";
        TimeType half = TimeType.HALF;
        String expectedTitle = String.format("\"%s\"가 %s 남았습니다.", editedTitle, half.getDescription());
        NotificationUpdateDto updateDto = NotificationUpdateDto.builder()
                .todoId(todo.getId())
                .title(editedTitle)
                .updaterId(member.getId())
                .timeType(half)
                .build();

        //when
        notificationService.update(updateDto);
        List<Notification> result = notificationRepository.findAll();
        //then
        assertThat(result).hasSize(1)
                .extracting("title","timeType","isRead","dueTime")
                .containsExactlyInAnyOrder(tuple(expectedTitle,updateDto.getTimeType(),false,dueDate));
    }

    @DisplayName("알림을 수정할 때, 작성자가 아닌 권한이 없는 유저가 수정할 경우 예외가 발생한다.")
    @Test
    void updateWithHasNotAuthorization(){
        //given
        Member writer = Member.builder()
                .nickName("writer")
                .profile("profile")
                .email("writer@email.com")
                .role(Role.USER)
                .fcmToken("writerfcmToken")
                .build();

        Member another = Member.builder()
                .nickName("another")
                .profile("profile")
                .email("another@email.com")
                .role(Role.USER)
                .fcmToken("anotherfcmToken")
                .build();

        memberRepository.saveAll(List.of(writer,another));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),
                startDate.getDayOfMonth(), startDate.getHour(), startDate.getMinute());
        TodoStatus status = TodoStatus.TODO;
        TodoPriority priority = TodoPriority.MEDIUM;
        Todo todo = Todo.builder()
                .member(writer)
                .title("제목")
                .description("설명")
                .startDate(startDate)
                .dueDate(dueDate)
                .status(status)
                .priority(priority)
                .build();
        todoRepository.save(todo);

        TimeType fifteen = TimeType.FIFTEEN;
        Notification notification = Notification.builder()
                .member(writer)
                .todo(todo)
                .dueTime(dueDate)
                .timeType(fifteen)
                .title("\"" + todo.getTitle() + "\"이" + fifteen.getDescription() + " 남았습니다.")
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .build();
        notificationRepository.save(notification);

        String editedTitle = "수정된 제목";
        NotificationUpdateDto updateDto = NotificationUpdateDto.builder()
                .todoId(todo.getId())
                .title(editedTitle)
                .updaterId(another.getId())
                .timeType(TimeType.HALF)
                .build();

        //when
        //then
        assertThatThrownBy(() -> notificationService.update(updateDto))
                .isInstanceOf(UnAuthorizationException.class)
                .hasMessageMatching("알림을 수정할 권한이 없습니다.");
    }

    @DisplayName("제거할 알림의 정보를 받아 알림을 삭제한다.")
    @Test
    void removeNotificationByTodoId(){
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

        TimeType fifteen = TimeType.FIFTEEN;
        Notification notification = Notification.builder()
                .member(member)
                .todo(todo)
                .dueTime(dueDate)
                .timeType(fifteen)
                .title("\"" + todo.getTitle() + "\"이" + fifteen.getDescription() + " 남았습니다.")
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .build();
        notificationRepository.save(notification);

        NotificationRemoveDto removeDto = NotificationRemoveDto.builder()
                .deleterId(member.getId())
                .todoId(todo.getId())
                .build();

        //when
        notificationService.removeNotificationByTodoId(removeDto);
        List<Notification> result = notificationRepository.findAll();
        //then
        assertThat(result).hasSize(0);
    }

    @DisplayName("알림을 수정할 때, 작성자가 아닌 권한이 없는 유저가 수정할 경우 예외가 발생한다.")
    @Test
    void removeNotificationByTodoIdWithNotAuthorization(){
        //given
        Member writer = Member.builder()
                .nickName("writer")
                .profile("profile")
                .email("writer@email.com")
                .role(Role.USER)
                .fcmToken("writerfcmToken")
                .build();

        Member another = Member.builder()
                .nickName("another")
                .profile("profile")
                .email("another@email.com")
                .role(Role.USER)
                .fcmToken("anotherfcmToken")
                .build();

        memberRepository.saveAll(List.of(writer,another));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),
                startDate.getDayOfMonth(), startDate.getHour(), startDate.getMinute());
        TodoStatus status = TodoStatus.TODO;
        TodoPriority priority = TodoPriority.MEDIUM;
        Todo todo = Todo.builder()
                .member(writer)
                .title("제목")
                .description("설명")
                .startDate(startDate)
                .dueDate(dueDate)
                .status(status)
                .priority(priority)
                .build();
        todoRepository.save(todo);

        TimeType fifteen = TimeType.FIFTEEN;
        Notification notification = Notification.builder()
                .member(writer)
                .todo(todo)
                .dueTime(dueDate)
                .timeType(fifteen)
                .title("\"" + todo.getTitle() + "\"이" + fifteen.getDescription() + " 남았습니다.")
                .content("해당 알림을 탭하시면 자세히 볼 수 있어요!")
                .build();
        notificationRepository.save(notification);

        NotificationRemoveDto removeDto = NotificationRemoveDto.builder()
                .deleterId(another.getId())
                .todoId(todo.getId())
                .build();

        //when
        //then
        assertThatThrownBy(() -> notificationService.removeNotificationByTodoId(removeDto))
                .isInstanceOf(UnAuthorizationException.class)
                .hasMessageMatching("알림을 제거할 권한이 없습니다.");
    }
}