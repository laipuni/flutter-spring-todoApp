package project.app.flutter_spring_todoapp.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank
    private String token;
}
