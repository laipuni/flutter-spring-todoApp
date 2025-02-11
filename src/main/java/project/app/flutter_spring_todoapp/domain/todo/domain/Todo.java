package project.app.flutter_spring_todoapp.domain.todo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

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
    private LocalDate startDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    @Enumerated(EnumType.STRING)
    private TodoPriority priority;

    @Builder
    private Todo(final String title, final String description,
                 final LocalDate startDate, final LocalDate dueDate,
                 final TodoStatus status, final TodoPriority priority) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }

    public static Todo of(final String title, final String description,
                     final LocalDate startDate, final LocalDate dueDate,
                     final TodoStatus status, final TodoPriority priority){
        return Todo.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(status)
                .priority(priority)
                .build();
    }

    public void update(final String title,final String description){
        this.title = StringUtils.hasText(title) ? title : "빈 제목";
        this.description = description;
    }

    public void changeStartDate(final LocalDate startDate){
        this.startDate = startDate;
    }


    public void changeDueDate(final LocalDate dueDate){
        this.dueDate = dueDate;
    }

    public void changeDate(final LocalDate startDate, final LocalDate dueDate){
        changeStartDate(startDate);
        changeDueDate(dueDate);
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
