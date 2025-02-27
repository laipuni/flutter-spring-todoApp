package project.app.flutter_spring_todoapp.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");
    final String roleName;

    public String getRoleName() {
        return "ROLE_" + this.roleName;
    }

}
