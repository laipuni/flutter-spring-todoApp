package project.app.flutter_spring_todoapp.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.exception.global.UnAuthorizationException;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.notification.dto.NotificationRemoveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationSaveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationUpdateDto;
import project.app.flutter_spring_todoapp.notification.repository.NotificationRepository;
import project.app.flutter_spring_todoapp.todo.TodoUpdateDto;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.repository.TodoRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final TodoRepository todoRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification save(final NotificationSaveDto saveDto){
        Todo todo = todoRepository.findById(saveDto.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 할일은 존재하지 않습니다."));
        Notification notification = notificationRepository.save(
                Notification.of(todo, todo.getMember(), todo.getDueDate(), saveDto.getTimeType(), saveDto.getTitle())
        );
        log.info("알림(id : {})을 생성했다.",notification.getId());
        return notification;
    }

    @Transactional
    public void update(final NotificationUpdateDto updateDto){
        Notification notification = notificationRepository.findNotificationByTodoId(updateDto.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 할일의 알림은 존재하지 않습니다."));
        if(!notification.isWriter(updateDto.getUpdaterId())){
            log.debug("사용자 (id : {})는 알림(id : {})을 수정할 권한이 없다.",updateDto.getUpdaterId(),notification.getId());
            throw new UnAuthorizationException("알림을 수정할 권한이 없습니다.");
        }
        notification.update(updateDto.getTitle(),updateDto.getTimeType());
    }

    public boolean hasNotificationForTodo(final Long todoId) {
        return notificationRepository.existsNotificationByTodoId(todoId);
    }

    @Transactional
    public void removeNotificationByTodoId(final NotificationRemoveDto removeDto) {
        Notification notification = notificationRepository.findNotificationByTodoId(removeDto.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 할일의 알림은 존재하지 않습니다."));
        if(!notification.isWriter(removeDto.getDeleterId())){
            log.debug("사용자 (id : {})는 알림(id : {})을 제거할 권한이 없다.",removeDto.getDeleterId(),notification.getId());
            throw new UnAuthorizationException("알림을 제거할 권한이 없습니다.");
        }
        notificationRepository.deleteAllByTodoId(removeDto.getTodoId());
        log.info("사용자 (id : {})는 알림(id : {})을 제거했다.",removeDto.getDeleterId(),notification.getId());
    }

    public Notification findByTodoId(final Long todoId) {
        return notificationRepository.findNotificationWithMemberByTodoId (todoId)
                .orElseThrow(() -> {
                    log.debug("해당 할일(id : {})의 알림은 존재하지 않습니다.",todoId);
                    throw new IllegalArgumentException("해당 알림은 존재하지 않습니다.");
                });
    }
}
