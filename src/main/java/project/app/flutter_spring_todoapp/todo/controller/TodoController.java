package project.app.flutter_spring_todoapp.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.app.flutter_spring_todoapp.api.ApiResponse;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.AddTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.DetailTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.TodoListResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.UpdateTodoResponse;
import project.app.flutter_spring_todoapp.todo.service.TodoService;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/api/todos")
    public ApiResponse<TodoListResponse> findTodos(){
        TodoListResponse response = todoService.findAll();
        return ApiResponse.ok(response);
    }

    @PostMapping("/api/todos")
    public ApiResponse<AddTodoResponse> addTodo(@Valid @RequestBody AddTodoRequest request){
        AddTodoResponse response = todoService.save(request);
        return ApiResponse.ok(response);
    }

    @GetMapping("/api/todos/{todoId}")
    public ApiResponse<DetailTodoResponse> detailTodo(@PathVariable("todoId") Long todoId){
        DetailTodoResponse response = todoService.detailTodo(todoId);
        return ApiResponse.ok(response);
    }

    @PutMapping("/api/todos")
    public ApiResponse<UpdateTodoResponse> updateTodo(@Valid @RequestBody UpdateTodoRequest request){
        UpdateTodoResponse response = todoService.update(request);
        return ApiResponse.ok(response);
    }
}
