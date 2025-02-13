package project.app.flutter_spring_todoapp.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultHandlersDsl;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.*;
import project.app.flutter_spring_todoapp.todo.service.TodoService;
import project.app.flutter_spring_todoapp.web.converter.config.WebMvcConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        TodoController.class
})
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("등록된 할일 조회요청 테스트")
    @Test
    void findTodos() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        //요청 값 선언
        String title = "할일1";
        String description = "할일1의 설명";
        TodoItemResponse todo = TodoItemResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.IN_PROGRESS)
                .priority(TodoPriority.MEDIUM)
                .build();

        List<TodoItemResponse> todoItemResponseList = List.of(todo);

        TodoListResponse response = TodoListResponse.builder()
                .size(todoItemResponseList.size())
                .todoList(todoItemResponseList)
                .build();

        Mockito.when(todoService.findAll()).thenReturn(response);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.size").value(response.getSize()))
                .andExpect(jsonPath("$.data.todoList").isArray())
                .andExpect(jsonPath("$.data.todoList[0].id").value(todo.getId()))
                .andExpect(jsonPath("$.data.todoList[0].title").value(todo.getTitle()))
                .andExpect(jsonPath("$.data.todoList[0].description").value(todo.getDescription()))
                .andExpect(jsonPath("$.data.todoList[0].startDate").value(todo.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.todoList[0].dueDate").value(todo.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.todoList[0].priority").value("MEDIUM"))
                .andExpect(jsonPath("$.data.todoList[0].status").value("IN_PROGRESS"));

    }

    @DisplayName("할일(Todo)을 추가요청 테스트")
    @Test
    void addTodo() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        //요청 값 선언
        String title = "할일1";
        String description = "할일1의 설명";

        AddTodoRequest request = AddTodoRequest.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .duetDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .build();

        //요청값 json으로 변환
        String body = objectMapper.writeValueAsString(request);

        AddTodoResponse response = AddTodoResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .status(TodoStatus.IN_PROGRESS)
                .build();

        Mockito.when(todoService.save(Mockito.any(AddTodoRequest.class)))
                        .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.description").value(response.getDescription()))
                .andExpect(jsonPath("$.data.startDate").value(response.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.dueDate").value(response.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }

    @DisplayName("할일(Todo)추가 요청을 받았을 때, 빈 제목을 받았을 경우 예외가 발생한다.")
    @Test
    void addTodoWithEmptyTitle() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        //요청 값 선언
        String title = ""; // 빈 제목 입력
        String description = "할일1의 설명";

        AddTodoRequest request = AddTodoRequest.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .duetDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .build();

        //요청값 json으로 변환
        String body = objectMapper.writeValueAsString(request);

        AddTodoResponse response = AddTodoResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .status(TodoStatus.IN_PROGRESS)
                .build();

        Mockito.when(todoService.save(Mockito.any(AddTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("제목은 필수 입력칸 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("할일(Todo)추가 요청을 받았을 때, 제한된 길이를 넘은 제목을 받았을 경우 예외가 발생한다.")
    @Test
    void addTodoWithWrongSizeTitle() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        //요청 값 선언
        String title = UUID.randomUUID().toString(); // 지정한 길이보다 긴 제목 입력
        String description = "할일1의 설명";

        AddTodoRequest request = AddTodoRequest.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .duetDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .build();

        //요청값 json으로 변환
        String body = objectMapper.writeValueAsString(request);

        AddTodoResponse response = AddTodoResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .status(TodoStatus.IN_PROGRESS)
                .build();

        Mockito.when(todoService.save(Mockito.any(AddTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("최대 32자까지 작성할 수 있습니다."))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @DisplayName("할일(Todo)추가 요청을 받았을 때, 시작 날짜가 없을 경우 예외가 발생한다.")
    @Test
    void addTodoWithNullStartDate() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        //요청 값 선언
        String title = "할일1"; // 지정한 길이보다 긴 제목 입력
        String description = "할일1의 설명";

        AddTodoRequest request = AddTodoRequest.builder()
                .title(title)
                .description(description)
                .startDate(null)
                .duetDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .build();

        //요청값 json으로 변환
        String body = objectMapper.writeValueAsString(request);

        AddTodoResponse response = AddTodoResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(TodoPriority.MEDIUM)
                .status(TodoStatus.IN_PROGRESS)
                .build();

        Mockito.when(todoService.save(Mockito.any(AddTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("시작 날짜는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @DisplayName("할일(Todo)추가 요청을 받았을 때, 마감 날짜가 없을 경우 예외가 발생한다.")
    @Test
    void addTodoWithNullDueDate() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();

        //요청 값 선언
        String title = "할일1"; // 지정한 길이보다 긴 제목 입력
        String description = "할일1의 설명";

        AddTodoRequest request = AddTodoRequest.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .duetDate(null)
                .priority(TodoPriority.MEDIUM)
                .build();

        //요청값 json으로 변환
        String body = objectMapper.writeValueAsString(request);

        AddTodoResponse response = AddTodoResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(null)
                .priority(TodoPriority.MEDIUM)
                .status(TodoStatus.IN_PROGRESS)
                .build();

        Mockito.when(todoService.save(Mockito.any(AddTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("마감 날짜는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @DisplayName("할일(Todo)추가 요청을 받았을 때, 우선 순위가 없을 경우 예외가 발생한다.")
    @Test
    void addTodoWithNullPriority() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        //요청 값 선언
        String title = "할일1"; // 지정한 길이보다 긴 제목 입력
        String description = "할일1의 설명";

        AddTodoRequest request = AddTodoRequest.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .duetDate(dueDate)
                .priority(null)
                .build();

        //요청값 json으로 변환
        String body = objectMapper.writeValueAsString(request);

        AddTodoResponse response = AddTodoResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(null)
                .status(TodoStatus.IN_PROGRESS)
                .build();

        Mockito.when(todoService.save(Mockito.any(AddTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("우선 순위는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("상세 할일(Todo) 조회요청 테스트")
    @Test
    void detailTodo() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        String title = "할일1";
        String description = "할일1의 설명";
        Long todoId = 1L;

        DetailTodoResponse response = DetailTodoResponse.builder()
                .id(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.IN_PROGRESS)
                .priority(TodoPriority.MEDIUM)
                .build();

        Mockito.when(todoService.detailTodo(Mockito.anyLong()))
                        .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/todos/%d",todoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.description").value(response.getDescription()))
                .andExpect(jsonPath("$.data.startDate").value(response.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.dueDate").value(response.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }

    @DisplayName("할일(Todo) 수정요청 테스트")
    @Test
    void updateTodo() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        String title = "변경된 할일 제목";
        String description = "변경된 할일의 설명";
        Long todoId = 1L;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.DONE)
                .priority(TodoPriority.HIGH)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.todoId").value(response.getTodoId()))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.description").value(response.getDescription()))
                .andExpect(jsonPath("$.data.startDate").value(response.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.dueDate").value(response.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.priority").value("HIGH"))
                .andExpect(jsonPath("$.data.status").value("DONE"));
    }

    @DisplayName("할일(Todo) 수정요청 테스트")
    @Test
    void updateTodoWithNullTodoId() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        String title = "변경된 할일 제목";
        String description = "변경된 할일의 설명";
        Long todoId = null;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.DONE)
                .priority(TodoPriority.HIGH)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("수정할 할일의 id는 필수 값 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("할일(Todo) 수정요청을 받았을 때, 할일 제목이 빈칸일 경우 예외가 발생한다.")
    @Test
    void updateTodoWithEmptyTitle() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        String title = "";// 빈제목 입력
        String description = "변경된 할일의 설명";
        Long todoId = 1L;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.DONE)
                .priority(TodoPriority.HIGH)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("제목은 필수 입력칸 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("할일(Todo) 수정요청을 받았을 때, 잘못된 길이의 제목일 경우 예외가 발생한다.")
    @Test
    void updateTodoWithWrongSizeTitle() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        String title = UUID.randomUUID().toString(); // 지정한 길이보다 긴 제목 입력
        String description = "변경된 할일의 설명";
        Long todoId = 1L;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.DONE)
                .priority(TodoPriority.HIGH)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("최대 32자까지 작성할 수 있습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("할일(Todo) 수정요청을 받았을 때, 시작 날짜가 null일 경우 예외가 발생한다.")
    @Test
    void updateTodoWithNullStartDate() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());

        String title = "변경된 할일 제목";
        String description = "변경된 할일의 설명";
        Long todoId = 1L;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(null)
                .dueDate(dueDate)
                .status(TodoStatus.DONE)
                .priority(TodoPriority.HIGH)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(null)
                .dueDate(dueDate)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("시작 날짜는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("할일(Todo) 수정요청을 받았을 때, 마감 날짜가 null일 경우 예외가 발생한다.")
    @Test
    void updateTodoWithNullDueDate() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();

        String title = "변경된 할일 제목";
        String description = "변경된 할일의 설명";
        Long todoId = 1L;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(null)
                .status(TodoStatus.DONE)
                .priority(TodoPriority.HIGH)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(null)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("마감 날짜는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("할일(Todo) 수정요청을 받았을 때, 우선 순위 값이 null일 경우 예외가 발생한다.")
    @Test
    void updateTodoWithNullPriority() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());


        String title = "변경된 할일 제목";
        String description = "변경된 할일의 설명";
        Long todoId = 1L;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(TodoStatus.DONE)
                .priority(null)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("우선 순위는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("할일(Todo) 수정요청을 받았을 때, 일의 상태 값이 null일 경우 예외가 발생한다.")
    @Test
    void updateTodoWithNullStatus() throws Exception {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(startDate.getYear() + 1,startDate.getMonth(),startDate.getDayOfMonth()
        ,startDate.getHour(),startDate.getMinute(),startDate.getSecond());


        String title = "변경된 할일 제목";
        String description = "변경된 할일의 설명";
        Long todoId = 1L;

        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .status(null)
                .priority(TodoPriority.HIGH)
                .build();

        String body = objectMapper.writeValueAsString(request);

        UpdateTodoResponse response = UpdateTodoResponse.builder()
                .todoId(todoId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .dueDate(dueDate)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Mockito.when(todoService.update(Mockito.any(UpdateTodoRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("일의 상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}