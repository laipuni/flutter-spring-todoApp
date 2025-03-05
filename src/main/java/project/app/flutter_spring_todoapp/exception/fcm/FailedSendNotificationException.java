package project.app.flutter_spring_todoapp.exception.fcm;

public class FailedSendNotificationException extends RuntimeException {
    public FailedSendNotificationException(final String message, final Throwable cause) {
        super(message,cause);
    }

    public FailedSendNotificationException(final String message) {
        super(message);
    }
}
