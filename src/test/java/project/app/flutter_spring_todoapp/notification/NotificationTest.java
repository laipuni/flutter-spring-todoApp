package project.app.flutter_spring_todoapp.notification;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @DisplayName("제목이 \"집안 청소\"인 할일의 알림을 생성할 때, \"집안 청소\" 30분 남았다고 알림 제목을 설정한다.")
    @Test
    void test(){
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

        LocalDateTime startDate = LocalDateTime.of(2025,3,1
                ,11,0,0,0);
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
                ,startDate.getHour(),startDate.getMinute(),0,0);

        Todo todo = Todo.builder()
                .title("할일 이름")
                .member(member)
                .status(TodoStatus.IN_PROGRESS)
                .startDate(startDate)
                .dueDate(dueDate)
                .description("할일 설명")
                .priority(TodoPriority.MEDIUM)
                .build();

        //when
        //then
        Notification notification = Notification.of(todo, member, dueDate, TimeType.HALF, todo.getTitle());
        Assertions.assertThat(notification.getTitle())
                .isEqualTo(String.format("\"%s\"가 %s 남았습니다.", todo.getTitle(), notification.getTimeType().getDescription()));
    }

}