package project.app.flutter_spring_todoapp.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("user"),
    ADMIN("admin");
    final String roleName;
}
