package project.app.flutter_spring_todoapp.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum TimeType {

    NONE("설정 없음","none"),
    FIVE("5분","five"),
    FIFTEEN("15분","fifteen"),
    HALF("30분","half"),
    HOUR("1시간","hour");

    final String description;
    final String time;
    final static Map<String, TimeType> timeTypeMap = Arrays.stream(TimeType.values())
            .collect(Collectors.toMap(TimeType::getTime, value -> value));

    @JsonCreator
    public static TimeType findTimeType(final String source) {
        return timeTypeMap.get(source.toLowerCase());
    }

    public static boolean isSet (final TimeType timeType){
        return timeType != null && !timeType.equals(TimeType.NONE);
    }
}
