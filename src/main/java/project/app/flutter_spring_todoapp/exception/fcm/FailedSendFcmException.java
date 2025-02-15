package project.app.flutter_spring_todoapp.exception.fcm;

public class FailedSendFcmException extends RuntimeException {
    public FailedSendFcmException(final String message, final Throwable cause) {
        super(message,cause);
    }

    public FailedSendFcmException(final String message) {
        super(message);
    }
}
