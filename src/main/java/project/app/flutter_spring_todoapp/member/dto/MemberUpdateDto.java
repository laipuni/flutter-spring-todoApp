package project.app.flutter_spring_todoapp.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUpdateDto {

    private Long memberId;
    private String nickname;
    private String email;
    private String token;
    private String profile;

    public static MemberUpdateDto of(final Long memberId, final String nickname,
                                     final String email, final String token,final String profile){
        return MemberUpdateDto.builder()
                .memberId(memberId)
                .nickname(nickname)
                .email(email)
                .token(token)
                .profile(profile)
                .build();
    }

}
