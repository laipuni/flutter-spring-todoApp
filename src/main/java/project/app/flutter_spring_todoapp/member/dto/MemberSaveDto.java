package project.app.flutter_spring_todoapp.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.app.flutter_spring_todoapp.member.Member;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSaveDto {

    private String nickname;
    private String email;
    private String token;
    private String profile;

    public static MemberSaveDto of(final String nickname, final String email, final String token,final String profile){
        return MemberSaveDto.builder()
                .nickname(nickname)
                .email(email)
                .token(token)
                .profile(profile)
                .build();
    }

    public Member toEntity(){
        return Member.of(this.nickname,this.email,this.token,this.profile);
    }

}
