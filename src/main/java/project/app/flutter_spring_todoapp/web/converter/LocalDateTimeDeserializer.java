package project.app.flutter_spring_todoapp.web.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER_SIX = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    private static final DateTimeFormatter FORMATTER_THREE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        try {
            LocalDateTime dateTime;
            if (date.length() > 23) {
                // 6자리 밀리초
                dateTime = LocalDateTime.parse(date, FORMATTER_SIX);
            } else {
                // 3자리 밀리초
                dateTime = LocalDateTime.parse(date, FORMATTER_THREE);
            }
            // 초와 밀리초를 0으로 초기화
            return dateTime.withSecond(0).withNano(0);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format: " + date, e);
        }
    }
}