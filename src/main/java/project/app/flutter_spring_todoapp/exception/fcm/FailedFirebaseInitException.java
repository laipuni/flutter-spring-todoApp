package project.app.flutter_spring_todoapp.exception.fcm;

public class FailedFirebaseInitException extends RuntimeException {
    public FailedFirebaseInitException(final String message) {
        super(message);
    }

    public FailedFirebaseInitException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
