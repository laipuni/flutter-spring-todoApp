package project.app.flutter_spring_todoapp.todo.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.threeten.bp.DateTimeUtils;
import project.app.flutter_spring_todoapp.member.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 32)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date",nullable = false)
    private LocalDateTime startDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    @Enumerated(EnumType.STRING)
    private TodoPriority priority;

    @JoinColumn(name = "member_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private Todo(final String title, final String description,
                 final LocalDateTime startDate, final LocalDateTime dueDate,
                 final TodoStatus status, final TodoPriority priority, final Member member) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.member = member;
    }

    public static Todo of(String title, final String description,
                          LocalDateTime startDate, LocalDateTime dueDate,
                          TodoStatus status, TodoPriority priority, final Member member
    ){
        // 기본값 설정
        startDate = (startDate != null) ? startDate : LocalDateTime.now();
        dueDate = (dueDate != null) ? dueDate : LocalDateTime.now().plusDays(1);
        status = (status != null) ? status : calculateStatus(startDate, dueDate);
        priority = (priority != null) ? priority : TodoPriority.LOW;

        return Todo.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(status)
                .priority(priority)
                .member(member)
                .build();
    }

    private static TodoStatus calculateStatus(final LocalDateTime startDate, final LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate)) {
            return TodoStatus.TODO;
        } else if (now.isAfter(dueDate)) {
            return TodoStatus.DONE;
        } else {
            return TodoStatus.IN_PROGRESS;
        }
    }

    public void update(final String title, final String description,
                       final LocalDateTime startDate, final LocalDateTime dueDate,
                       final TodoStatus status, final TodoPriority priority){
        this.title = StringUtils.hasText(title) ? title : this.title;
        this.description = description != null ? description : this.description;
        this.startDate = startDate != null ? startDate : this.startDate;
        this.dueDate = dueDate != null ? dueDate : this.dueDate;
        this.status = status != null ? status : this.status;
        this.priority = priority != null ? priority : this.priority;
    }

    public boolean isWriter(final Long memberId){
        return this.member.getId().equals(memberId);
    }
}
