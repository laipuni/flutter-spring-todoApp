package project.app.flutter_spring_todoapp.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.todo.domain.Todo;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n join fetch n.member where n.todo.id = :todoId")
    Optional<Notification> findNotificationWithMemberByTodoId (@Param("todoId") Long todoId);

    @Query("select n from Notification n where n.todo.id = :todoId")
    Optional<Notification> findNotificationByTodoId(@Param("todoId") Long todoId);

    @Query("select exists (select 1 from Notification n where n.todo.id = :todoId)")
    boolean existsNotificationByTodoId(@Param("todoId") Long todoId);

    @Modifying
    @Query("delete from Notification n where n.todo.id = :todoId")
    void deleteAllByTodoId(@Param("todoId") Long todoId);
}
