package project.app.flutter_spring_todoapp.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.app.flutter_spring_todoapp.api.ApiResponse;
import project.app.flutter_spring_todoapp.member.dto.reqeust.ChangeFcmTokenRequest;
import project.app.flutter_spring_todoapp.member.service.MemberServiceImpl;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberService;

}
