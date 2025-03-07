package project.app.flutter_spring_todoapp.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.member.service.MemberService;
import project.app.flutter_spring_todoapp.notification.Notification;
import project.app.flutter_spring_todoapp.notification.TimeType;
import project.app.flutter_spring_todoapp.notification.dto.NotificationRemoveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationSaveDto;
import project.app.flutter_spring_todoapp.notification.dto.NotificationUpdateDto;
import project.app.flutter_spring_todoapp.notification.service.NotificationService;
import project.app.flutter_spring_todoapp.redis.RedisService;
import project.app.flutter_spring_todoapp.redis.ReminderMessage;
import project.app.flutter_spring_todoapp.redis.UpdateReminderMessage;
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

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final TodoService todoService;
    private final NotificationService notificationService;
    private final RedisService redisService;

    @Transactional
    public AddTodoResponse createTodoWithNotification(final AddTodoRequest request, final SessionMember sessionMember){
        Todo todo = todoService.save(createTodoSaveDto(request), sessionMember.getMemberSeq());
        TimeType timeType = request.getTimeType();
        if(TimeType.isSet(timeType)){
            // 할일에 시간을 설정한 경우
            Notification notification = notificationService.save(NotificationSaveDto.of(todo.getId(), request.getTitle(), request.getDueDate(), timeType));
            // redis ttl key 설정
            ReminderMessage message = ReminderMessage.of(notification);
            redisService.saveReminder(message, message.getNotificationSecond());
        }
        return AddTodoResponse.of(todo,timeType);
    }

    private static TodoSaveDto createTodoSaveDto(final AddTodoRequest request) {
        return TodoSaveDto.of(request.getTitle(), request.getDescription(), request.getStartDate(),
                request.getDueDate(), request.getStatus(), request.getPriority());
    }

    @Transactional
    public UpdateTodoResponse updateTodoWithNotification(final UpdateTodoRequest request, final SessionMember sessionMember){
        Todo todo = todoService.update(createUpdateTodoReqeust(request, sessionMember.getMemberSeq()));
        boolean existNotificationForTodo = notificationService.hasNotificationForTodo(request.getTodoId());
        boolean hasNotification = TimeType.isSet(request.getTimeType());
        handleNotificationChanges(request,existNotificationForTodo, hasNotification, sessionMember.getMemberSeq());
        return UpdateTodoResponse.of(todo);
    }

    private static TodoUpdateDto createUpdateTodoReqeust(final UpdateTodoRequest request,final Long updaterId) {
        return TodoUpdateDto.of(request.getTodoId(), request.getTitle(), request.getDescription(),
                request.getStartDate(), request.getDueDate(), request.getPriority(), request.getStatus(),updaterId);
    }

    private void handleNotificationChanges(final UpdateTodoRequest request, final boolean existNotificationForTodo,
                                           final boolean hasNotification, final Long memberId) {
        if (!existNotificationForTodo && hasNotification) {
            // 알림 X -> 알림 O (알림 추가)
            saveNotificationWithReminder(request);
        } else if (existNotificationForTodo && !hasNotification) {
            // 알림 O -> 알림 X (알림 제거)
            removeNotificationWithReminder(request.getTodoId(), memberId);
        } else {
            // 알림 O -> 알림 O (알림 내용 변경)
            updateNotificationWithReminder(request, memberId);
        }
    }

    private void updateNotificationWithReminder(final UpdateTodoRequest request,final Long memberId) {
        Notification notification = notificationService.findByTodoId(request.getTodoId());
        notificationService.update(NotificationUpdateDto.of(request.getTodoId(), request.getTitle(), request.getTimeType(), memberId));
        if(notification.changeTimeType(request.getTimeType()) || notification.changeDueDate(request.getDueDate())){
            //알림 시간 타입 or 마감 시간을 변경 했을 경우
            //redis에 ttl key 제거 후, 생성
            redisService.updateReminder(UpdateReminderMessage
                    .of(notification.getId(), request.getDueDate(),request.getTimeType()));
        }
    }

    private void saveNotificationWithReminder(final UpdateTodoRequest request) {
        Notification savedNotification = notificationService.save(NotificationSaveDto.of(request.getTodoId(), request.getTitle(),
                request.getDueDate(), request.getTimeType()));
        //redis에 ttl key 생성
        ReminderMessage reminderMessage = ReminderMessage.of(savedNotification);
        redisService.saveReminder(reminderMessage, reminderMessage.getNotificationSecond());
    }

    @Transactional
    public DeleteTodoResponse removeTodoWithNotification(final DeleteTodoRequest request, final SessionMember sessionMember){
        //redis에 저장된 ttl 알림 제거
        removeNotificationWithReminder(request.getTodoId(), sessionMember.getMemberSeq());
        todoService.removeTodoBy(TodoDeleteDto.of(request.getTodoId(),sessionMember.getMemberSeq()));
        return DeleteTodoResponse.of(request.getTodoId());
    }

    private void removeNotificationWithReminder(final Long todoId, final Long memberId) {
        Notification notification = notificationService.findByTodoId(todoId);
        redisService.deleteReminder(notification.getId());
        //redis에 ttl key 제거
        notificationService.removeNotificationByTodoId(NotificationRemoveDto.of(todoId, memberId));
    }
}
