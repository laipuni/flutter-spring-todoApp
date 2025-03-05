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

    NONE("설정 없음","none",0L),
    FIVE("5분","five",5L),
    FIFTEEN("15분","fifteen",15L),
    HALF("30분","half",30L),
    HOUR("1시간","hour",60L);

    final String description;
    final String name;
    final Long time;
    final static Map<String, TimeType> timeTypeMap = Arrays.stream(TimeType.values())
            .collect(Collectors.toMap(TimeType::getName, value -> value));

    @JsonCreator
    public static TimeType findTimeType(final String source) {
        return timeTypeMap.get(source.toLowerCase());
    }

    public static boolean isSet (final TimeType timeType){
        return timeType != null && !timeType.equals(TimeType.NONE);
    }
}
