package project.app.flutter_spring_todoapp.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.exception.global.UnAuthorizationException;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.todo.TodoUpdateDto;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.dto.TodoDeleteDto;
import project.app.flutter_spring_todoapp.todo.dto.TodoSaveDto;
import project.app.flutter_spring_todoapp.todo.dto.response.*;
import project.app.flutter_spring_todoapp.todo.repository.TodoRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;


    @Transactional
    @Override
    public Todo save(final TodoSaveDto todoServiceDto, final Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.trace("사용자(id : {})가 존재하지 않습니다.",memberId);
                    return new IllegalArgumentException("해당 할일은 존재하지 않습니다.");
                });
        Todo todo = todoRepository.save(todoServiceDto.toEntity(member));
        log.info("사용자(id:{})가 할일(id:{}) \"{}\"을 생성했다",memberId, todo.getId(),todo.getTitle());
        return todo;
    }

    @Override
    public TodoListResponse findAll(final Long memberId) {
        return TodoListResponse.of(todoRepository.findAllByMemberIdOrderByIdDesc(memberId));
    }

    @Transactional
    @Override
    public Todo update(final TodoUpdateDto request) {
        Todo todo = todoRepository.findById(request.getTodoId())
                .orElseThrow(() -> {
                    log.trace("알림(id : {})은 존재하지 않습니다.",request.getTodoId());
                    return new IllegalArgumentException("해당 할일은 존재하지 않습니다.");
                });
        if(!todo.isWriter(request.getUpdaterId())){
            log.debug("사용자(id :{})는 할일(id : {})를 삭제할 권한이 없다", request.getUpdaterId(), request.getTodoId());
            throw new UnAuthorizationException("할일을 제거할 권한이 없습니다.");
        }
        todo.update(
                request.getTitle(),request.getDescription(),
                request.getStartDate(),request.getDueDate(),
                request.getStatus(),request.getPriority()
        );
        log.info("사용자(id :{})는 할일(id : {})을 수정했다.", request.getUpdaterId(), request.getTodoId());
        return todo;
    }

    @Override
    public DetailTodoResponse detailTodo(final Long todoId) {
        return todoRepository.findTodoDetailByTodoId(todoId)
                .orElseThrow(() -> {
                    log.trace("알림(id : {})은 존재하지 않습니다.",todoId);
                    return new IllegalArgumentException("해당 할일은 존재하지 않습니다.");
                });
    }

    @Transactional
    @Override
    public DeleteTodoResponse removeTodoBy(final TodoDeleteDto deleteDto) {
        Long todoId = deleteDto.getTodoId();
        Long deleterId = deleteDto.getDeleterId();
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> {
                    log.trace("알림(id : {})은 존재하지 않습니다.",todoId);
                    return new IllegalArgumentException("해당 할일은 존재하지 않습니다.");
                });
        if(!todo.isWriter(deleterId)){
            log.debug("사용자(id :{})는 할일(id : {})를 삭제할 권한이 없다", deleterId, todoId);
            throw new UnAuthorizationException("할일을 제거할 권한이 없습니다.");
        }
        todoRepository.deleteById(todoId);
        log.info("할일(id : {})를 삭제했다.", todoId);
        return DeleteTodoResponse.of(todoId);
    }
}
