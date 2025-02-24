package project.app.flutter_spring_todoapp.exception.global;

public class FailedResolveSessionMemberException extends RuntimeException {
    public FailedResolveSessionMemberException(final String message) {
        super(message);
    }

    public FailedResolveSessionMemberException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
