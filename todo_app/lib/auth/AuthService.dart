import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:todo_app/service/SecureStorageService.dart';

class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final GoogleSignIn _googleSignIn = GoogleSignIn(
    serverClientId: "34520190078-k1vddv5mua07gt5hd6d3hgacr7uasp98.apps.googleusercontent.com", // 🔹 생성한 Web 클라이언트 ID 입력
  );

  /// 🔹 Google 로그인 실행
  Future<User?> signInWithGoogle() async {
    final GoogleSignInAccount? googleUser = await _googleSignIn.signIn();
    if (googleUser == null) return null; // 로그인 취소

    final GoogleSignInAuthentication googleAuth = await googleUser.authentication;
    final AuthCredential credential = GoogleAuthProvider.credential(
      accessToken: googleAuth.accessToken,
      idToken: googleAuth.idToken,
    );

    UserCredential userCredential = await _auth.signInWithCredential(credential);
    final User? user = userCredential.user;
    if (user != null) {
      final String? idToken = await user.getIdToken();
      final String? refreshToken = userCredential.credential?.accessToken; // 🔹 Firebase는 refresh_token을 accessToken에 저장

      if (idToken != null && refreshToken != null) {
        await SecureStorageService().saveAccessToken(idToken);
        await SecureStorageService().saveRefreshToken(refreshToken);
      }
    }
    return user;
  }

  /// 🔹 Firebase ID 토큰 + FCM 토큰을 백엔드로 전송
  Future<void> sendTokenToBackend(String? idToken, String? fcmToken) async {
    if (idToken == null) return;
    final response = await http.post(
      Uri.parse("http://10.0.2.2:8080/api/auth/google"),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $idToken',
      },
      body: jsonEncode({'token': fcmToken}),
    );
    if (response.statusCode == 200) {
      print("백엔드 로그인 성공: ${response.body}");
    } else {
      print("백엔드 로그인 실패");
    }
  }

  //  SharedPreferencesService를 사용하여 로그인 여부 확인
  static Future<bool> isAuthenticated() async {
    String? token = await SecureStorageService().getAccessToken();
    return token != null;
  }

  /// 🔹 로그아웃 기능
  Future<void> signOut() async {
    await SecureStorageService().clearTokens();
    await _googleSignIn.signOut();
    await _auth.signOut();
  }
}
