package project.app.flutter_spring_todoapp.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.*;
import project.app.flutter_spring_todoapp.todo.repository.TodoRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;

    @Transactional
    @Override
    public AddTodoResponse save(final AddTodoRequest request) {
        Todo todo = todoRepository.save(request.toEntity());
        log.info("할일(id:{}) \"{}\"을 생성했다",todo.getId(),todo.getTitle());
        return AddTodoResponse.of(todo);
    }

    @Override
    public TodoListResponse findAll() {
        return TodoListResponse.of(todoRepository.findAll());
    }

    @Transactional
    @Override
    public UpdateTodoResponse update(final UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(request.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 할일은 존재하지 않습니다."));
        todo.update(
                request.getTitle(),request.getDescription(),
                request.getStartDate(),request.getDueDate(),
                request.getStatus(),request.getPriority()
        );
        log.info("할일(id : {})가 수정됐다.");
        return UpdateTodoResponse.of(todo);
    }

    @Override
    public DetailTodoResponse detailTodo(final Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 할일은 존재하지 않습니다."));
        return DetailTodoResponse.of(todo);
    }

    @Transactional
    @Override
    public DeleteTodoResponse removeTodoBy(final Long todoId) {
        todoRepository.deleteById(todoId);
        log.info("할일(id : {})를 삭제했다.",todoId);
        return DeleteTodoResponse.of(todoId);
    }
}
