package project.app.flutter_spring_todoapp.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.app.flutter_spring_todoapp.api.ApiResponse;
import project.app.flutter_spring_todoapp.security.oauth2.dto.SessionMember;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.DeleteTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.*;
import project.app.flutter_spring_todoapp.todo.service.ReservationService;
import project.app.flutter_spring_todoapp.todo.service.TodoService;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final ReservationService reservationService;

    @GetMapping("/api/todos")
    public ApiResponse<TodoListResponse> findTodos(SessionMember sessionMember){
        TodoListResponse response = todoService.findAll(sessionMember.getMemberSeq());
        return ApiResponse.ok(response);
    }

    @GetMapping("/api/v2/todos")
    public ApiResponse<TodoListResponse> findTodosV2(@RequestParam(value = "search", defaultValue = "") String search,
                                                     @RequestParam(value = "page",defaultValue = "0") int page,
                                                     @RequestParam(value = "order", defaultValue = "latest") String order,
                                                     @RequestParam(value = "sort",defaultValue = "desc") String sort,
                                                     SessionMember sessionMember){
        TodoListResponse response = todoService.findAll(sessionMember.getMemberSeq(),page,search,order,sort);
        return ApiResponse.ok(response);
    }

    @PostMapping("/api/todos")
    public ApiResponse<AddTodoResponse> addTodo(@Valid @RequestBody AddTodoRequest request,SessionMember sessionMember){
        AddTodoResponse response = reservationService.createTodoWithNotification(request,sessionMember);
        return ApiResponse.ok(response);
    }

    @GetMapping("/api/todos/{todoId}")
    public ApiResponse<DetailTodoResponse> detailTodo(@PathVariable("todoId") Long todoId, SessionMember sessionMember){
        DetailTodoResponse response = todoService.detailTodo(todoId);
        return ApiResponse.ok(response);
    }

    @PutMapping("/api/todos")
    public ApiResponse<UpdateTodoResponse> updateTodo(@Valid @RequestBody UpdateTodoRequest request, SessionMember sessionMember){
        UpdateTodoResponse response = reservationService.updateTodoWithNotification(request,sessionMember);
        return ApiResponse.ok(response);
    }

    @DeleteMapping("/api/todos")
    public ApiResponse<DeleteTodoResponse> deleteTodo(@Valid @RequestBody DeleteTodoRequest request, SessionMember sessionMember){
        DeleteTodoResponse response = reservationService.removeTodoWithNotification(request,sessionMember);
        return ApiResponse.ok(response);
    }
}
