package project.app.flutter_spring_todoapp.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeType {

    NONE("Not Notification","None"),
    FIVE("five minutes","5분"),
    FIFTEEN("fifteen minutes","15분"),
    HALF("half minutes","30분"),
    HOUR("a hour","1시간");

    final String description;
    final String time;

    public static boolean isSet (final TimeType timeType){
        return timeType != null && !timeType.equals(TimeType.NONE);
    }
}
