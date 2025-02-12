package project.app.flutter_spring_todoapp.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.app.flutter_spring_todoapp.exception.api.ApiErrorResponse;

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
}
