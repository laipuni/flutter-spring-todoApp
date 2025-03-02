package project.app.flutter_spring_todoapp.notification;

import ch.qos.logback.core.util.StringUtil;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.todo.domain.Todo;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "title", length = 64, nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "time_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeType timeType; // 지정하지 않을 경우 NONE을 설정

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false; // 알림을 읽음 여부

    @Column(name = "due_time", nullable = false)
    private LocalDateTime dueTime;

    @JoinColumn(name = "member_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "todo_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Todo todo;

    @Builder
    private Notification(final String title, final String content, final TimeType timeType,
                        final LocalDateTime dueTime, final Member member,
                        final Todo todo) {
        this.title = title;
        this.content = content;
        this.timeType = timeType;
        this.dueTime = dueTime;
        this.isRead = false;
        this.member = member;
        this.todo = todo;
    }

    public static Notification of(final Todo todo, final Member member, final LocalDateTime dueTime,
                                  final TimeType timeType, String title){
        return of(todo, member, dueTime, timeType, title,null);
    }

    public static Notification of(final Todo todo, final Member member, final LocalDateTime dueTime,
                                  final TimeType timeType, String title, @Nullable String content){
        if (!StringUtils.hasText(title)) {
            // 제목이 없으면 기본 제목 설정
            title = todo.getTitle();
        }
        title = createNotificationTitle(title,timeType);
        content = StringUtils.hasText(content) ? content : createNotificationContent();
        return Notification.builder()
                .title(title)
                .content(content)
                .todo(todo)
                .member(member)
                .dueTime(dueTime)
                .timeType(timeType)
                .build();
    }

    public boolean isWriter(final Long writerId){
        return this.member.getId().equals(writerId);
    }
    private static String createNotificationTitle(final String todoTitle, final TimeType timeType){
        return String.format("\"%s\"가 %s 남았습니다.", todoTitle, timeType.getDescription());
    }

    private static String createNotificationContent(){
        return "해당 알림을 탭하시면 자세히 볼 수 있어요!";
    }

    public void read(){
        this.isRead = true;
    }

    public void update(final String title, final TimeType timeType) {
        if(TimeType.isSet(timeType) && StringUtils.hasText(title)){
            this.title = createNotificationTitle(title,timeType);
            this.timeType = timeType;
        }
    }
}
