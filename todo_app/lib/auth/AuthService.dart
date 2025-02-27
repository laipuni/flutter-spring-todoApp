import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

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
    return userCredential.user;
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

  /// 🔹 로그아웃 기능
  Future<void> signOut() async {
    await _googleSignIn.signOut();
    await _auth.signOut();
  }
}
