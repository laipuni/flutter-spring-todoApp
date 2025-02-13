package project.app.flutter_spring_todoapp.todo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.app.flutter_spring_todoapp.todo.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
