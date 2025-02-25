package project.app.flutter_spring_todoapp.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.notification.dto.NotificationRemoveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationSaveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationUpdateDto;
import project.app.flutter_spring_todoapp.notification.service.NotificationService;
import project.app.flutter_spring_todoapp.security.oauth2.dto.SessionMember;
import project.app.flutter_spring_todoapp.todo.TodoUpdateDto;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.dto.TodoDeleteDto;
import project.app.flutter_spring_todoapp.todo.dto.TodoSaveDto;
import project.app.flutter_spring_todoapp.todo.dto.request.AddTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.DeleteTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.request.UpdateTodoRequest;
import project.app.flutter_spring_todoapp.todo.dto.response.AddTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.DeleteTodoResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.UpdateTodoResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final TodoService todoService;
    private final NotificationService notificationService;

    @Transactional
    public AddTodoResponse createTodoWithNotification(final AddTodoRequest request, final SessionMember sessionMember){
        Todo todo = todoService.save(createTodoSaveDto(request), sessionMember.getMemberSeq());
        TimeType timeType = request.getTimeType();
        if(TimeType.isSet(timeType)){
            // 할일에 시간을 설정한 경우
            notificationService.save(NotificationSaveDto.of(todo.getId(), request.getTitle(),request.getDuetDate(),timeType));
        }
        return AddTodoResponse.of(todo,timeType);
    }

    private static TodoSaveDto createTodoSaveDto(final AddTodoRequest request) {
        return TodoSaveDto.of(request.getTitle(), request.getDescription(), request.getStartDate(),
                request.getDuetDate(), request.getStatus(), request.getPriority());
    }

    @Transactional
    public UpdateTodoResponse updateTodoWithNotification(final UpdateTodoRequest request, final SessionMember sessionMember){
        Todo todo = todoService.update(createUpdateTodoReqeust(request, sessionMember.getMemberSeq()));
        boolean existNotification = notificationService.hasNotificationForTodo(request.getTodoId());
        boolean hasNotification = TimeType.isSet(request.getTimeType());
        handleNotificationChanges(request,existNotification,hasNotification, sessionMember.getMemberSeq());
        return UpdateTodoResponse.of(todo);
    }

    private static TodoUpdateDto createUpdateTodoReqeust(final UpdateTodoRequest request,final Long updaterId) {
        return TodoUpdateDto.of(request.getTodoId(), request.getTitle(), request.getDescription(),
                request.getStartDate(), request.getDueDate(), request.getPriority(), request.getStatus(),updaterId);
    }

    private void handleNotificationChanges(final UpdateTodoRequest request, final boolean existNotification,
                                           final boolean hasNotification, final Long memberId) {
        if (!existNotification && hasNotification) {
            // 알림 X -> 알림 O (알림 추가)
            notificationService.save(NotificationSaveDto.of(request.getTodoId(),request.getTitle(),request.getDueDate(),request.getTimeType()));
        } else if (existNotification && !hasNotification) {
            // 알림 O -> 알림 X (알림 제거)
            notificationService.removeNotificationByTodoId(NotificationRemoveDto.of(request.getTodoId(),memberId));
        } else if (existNotification) {
            // 알림 O -> 알림 O (알림 내용 변경)
            notificationService.update(NotificationUpdateDto.of(request.getTodoId(),request.getTitle(),request.getTimeType(),memberId));
        }
    }

    @Transactional
    public DeleteTodoResponse removeTodoWithNotification(final DeleteTodoRequest request, final SessionMember sessionMember){
        notificationService.removeNotificationByTodoId(NotificationRemoveDto.of(request.getTodoId(), sessionMember.getMemberSeq()));
        todoService.removeTodoBy(TodoDeleteDto.of(request.getTodoId(),sessionMember.getMemberSeq()));
        return DeleteTodoResponse.of(request.getTodoId());
    }
}
