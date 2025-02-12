package project.app.flutter_spring_todoapp.exception.api;

import org.springframework.http.HttpStatus;
import project.app.flutter_spring_todoapp.api.ApiResponse;

public class ApiErrorResponse<T> {

    private HttpStatus status;
    private int code;
    private String message;
    private T data;

    public ApiErrorResponse(final HttpStatus status, final String message, final int code, final T data) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static <T> ApiErrorResponse<T> badRequest(final String message, final T data){
        return new ApiErrorResponse<>(HttpStatus.BAD_REQUEST, message,
                HttpStatus.BAD_REQUEST.value(), data);
    }
}
