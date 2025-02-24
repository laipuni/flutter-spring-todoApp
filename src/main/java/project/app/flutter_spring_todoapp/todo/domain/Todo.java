package project.app.flutter_spring_todoapp.todo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import project.app.flutter_spring_todoapp.member.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
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

    public static Todo of(final String title, final String description,
                          final LocalDateTime startDate, final LocalDateTime dueDate,
                          final TodoPriority priority, final Member member){

        return of(title,description,startDate,dueDate,null,priority,member);
    }

    public static Todo of(final String title, final String description,
                          final LocalDateTime startDate, final LocalDateTime dueDate,
                          TodoStatus status, final TodoPriority priority,final Member member
    ){
        if(status == null){
            //사용자가 상태를 설정하지 않았을 경우 지정한 날짜에 따라 상태를 지정함
            status = calculateStatus(startDate, dueDate);
        }

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
        this.title = StringUtils.hasText(title) ? title : "빈 제목";
        this.description = description;
        changeDate(startDate,dueDate);
        this.status = status != null ? status : this.status;
        this.priority = priority != null ? priority : this.priority;
    }

    public void changeStartDate(final LocalDateTime startDate){
        this.startDate = startDate;
    }


    public void changeDueDate(final LocalDateTime dueDate){
        this.dueDate = dueDate;
    }

    public void changeDate(final LocalDateTime startDate, final LocalDateTime dueDate){
        changeStartDate(startDate);
        changeDueDate(dueDate);
    }

    public boolean isWriter(final Long memberId){
        return this.member.getId().equals(memberId);
    }

    public void done(){
        this.status = TodoStatus.DONE;
    }

    public void todo(){
        this.status = TodoStatus.TODO;
    }

    public void inProgress(){
        this.status = TodoStatus.IN_PROGRESS;
    }

    public boolean isDone(){
        return this.status.equals(TodoStatus.DONE);
    }
}
