package project.app.flutter_spring_todoapp.todo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.dto.response.DetailTodoResponse;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    List<Todo> findAllByMemberIdOrderByIdDesc(Long memberId);

    @Query("select new project.app.flutter_spring_todoapp.todo.dto.response." +
            "DetailTodoResponse(t.id, t.title, t.description, t.startDate, t.dueDate, t.status, t.priority, n.timeType) " +
            "from Todo t " +
            "left join Notification n on n.todo.id = t.id " +
            "where t.id = :todoId"
    )
    Optional<DetailTodoResponse> findTodoDetailByTodoId(@Param("todoId") Long todoId);

}
