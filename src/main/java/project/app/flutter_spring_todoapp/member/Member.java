package project.app.flutter_spring_todoapp.member;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String fcmToken;

    @Column(nullable = false, length = 20, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String email;

    private String profile;

    @Builder
    private Member(final Role role, final String fcmToken, final String nickName, final String email, final String profile) {
        this.role = role;
        this.fcmToken = fcmToken;
        this.nickName = nickName;
        this.email = email;
        this.profile = profile;
    }

    public String getRoleName(){
        return this.role.getRoleName();
    }

    public Member update(final String nickName, final String email, final String profile){
        this.nickName = StringUtils.hasText(nickName) ? nickName : this.nickName;
        this.nickName = StringUtils.hasText(email) ? email : this.email;
        this.nickName = StringUtils.hasText(profile) ? profile : this.profile;
        return this;
    }

    public void changeFcmToken(final String token){
        if(!StringUtils.hasText(token)){
            return;
        }
        this.fcmToken = token;
    }
}
