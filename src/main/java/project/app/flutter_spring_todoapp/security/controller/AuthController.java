package project.app.flutter_spring_todoapp.security.controller;


import com.google.firebase.auth.FirebaseAuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.app.flutter_spring_todoapp.api.ApiResponse;
import project.app.flutter_spring_todoapp.exception.global.LoginRequiredException;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.security.controller.request.LoginRequest;
import project.app.flutter_spring_todoapp.security.service.AuthService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/google")
    public ApiResponse<Member> loginWithGoogle(@RequestHeader("Authorization") String authHeader,
                                               @Valid @RequestBody LoginRequest request) {
        String idToken = authHeader.replace("Bearer ", "");
        String fcmToken = request.getToken();
        Member member = authService.authenticateUser(idToken, fcmToken);
        return ApiResponse.ok(member);
    }
}