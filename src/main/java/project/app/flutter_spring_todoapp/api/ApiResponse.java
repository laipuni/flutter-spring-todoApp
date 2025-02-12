package project.app.flutter_spring_todoapp.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private HttpStatus status;
    private int code;
    private T data;

    public ApiResponse(final HttpStatus status, final int code, final T data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public static <T>ApiResponse<T> ok(final T data){
        return new ApiResponse<>(HttpStatus.OK,HttpStatus.OK.value(), data);
    }
}
