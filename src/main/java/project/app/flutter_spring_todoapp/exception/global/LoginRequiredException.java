package project.app.flutter_spring_todoapp.exception.global;

public class LoginRequiredException extends RuntimeException {
    public LoginRequiredException(final String message) {
        super(message);
    }

    public LoginRequiredException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
