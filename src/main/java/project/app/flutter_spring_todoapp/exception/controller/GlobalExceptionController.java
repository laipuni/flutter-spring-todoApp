package project.app.flutter_spring_todoapp.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.app.flutter_spring_todoapp.exception.global.FailedResolveSessionMemberException;
import project.app.flutter_spring_todoapp.exception.api.ApiErrorResponse;
import project.app.flutter_spring_todoapp.exception.fcm.FailedSendFcmException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<Object> bindException(final BindException e){
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.debug("[bindException] msg = {}", message);
        return ApiErrorResponse.badRequest(
                message,
                null
        );
    }

    @ExceptionHandler(FailedSendFcmException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse<Object> FailedSendFcmException(final FailedSendFcmException e){
        log.error("[FailedSendFcmException] fcm 메세지 전송 실패 msg = {}, cause", e.getMessage(), e.getCause());
        //개발자에게 slack 메시지 혹은 이메일로 알림을 전송해야 함
        return ApiErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler(FailedResolveSessionMemberException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse<Object> FailedSendFcmException(final FailedResolveSessionMemberException e){
        log.debug("[FailedResolveSessionMemberException] Session does not contain a valid SessionMember attribute.");
        return ApiErrorResponse.of(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                null
        );
    }
}
