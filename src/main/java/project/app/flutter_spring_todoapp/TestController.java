package project.app.flutter_spring_todoapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
//@CrossOrigin(origins = "*") // CORS 허용
public class TestController {

    @GetMapping("/api/flutter-test")
    public String httpTest(){
        log.info("Flutter Rest Api Ok!");
        return "ok";
    }

    @GetMapping(value = "/api/flutter-listTest")
    public ResponseEntity<List<TestDto>> listTest(){
        List<TestDto> dtoList = List.of(
                new TestDto(1L, "제목1", "내용1"),
                new TestDto(2L, "제목2", "내용2"),
                new TestDto(3L, "제목3", "내용3"),
                new TestDto(4L, "제목4", "내용4")
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String response = getDto(objectMapper, dtoList);
        log.info("send {} to Flutter!", response);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    private String getDto(final ObjectMapper objectMapper, final List<TestDto> dtoList) {
        try {
            return objectMapper.writeValueAsString(dtoList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
