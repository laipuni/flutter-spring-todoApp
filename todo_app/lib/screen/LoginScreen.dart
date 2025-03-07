import 'dart:convert';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:todo_app/HostName.dart';
import 'package:todo_app/RouteName.dart';
import 'package:todo_app/auth/AuthService.dart';
import 'package:todo_app/http/HttpInterceptor.dart';
import 'package:todo_app/service/SharedPreferencesService.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final SharedPreferencesService _sharedPreferencesService = SharedPreferencesService();
  final AuthService _authService = AuthService();
  User? _currentUser;

  /// Google 로그인 실행
  Future<void> handleGoogleLogin() async {
    User? user = await _authService.signInWithGoogle();
    if (user != null) {
      String? idToken = await user.getIdToken();
      String? token = await _sharedPreferencesService.getFcmToken();
      await sendTokenToBackend(idToken, token);
      setState(() {
        _currentUser = user;
      });
      //이전에 접근했던 url로 다시 돌아가도록 설정
      String? redirectUrl = await _sharedPreferencesService.getRedirectUrl();
      Navigator.pushReplacementNamed(context, redirectUrl ?? RouteName.home);
    }
  }

  ///Firebase ID 토큰 + FCM 토큰을 백엔드로 전송
  Future<void> sendTokenToBackend(String? idToken, String? fcmToken) async {
    if (idToken == null) return;
    Map<String,dynamic> response = await HttpInterceptor(context).post(
      Uri.parse("${HostName.host}/api/auth/google"),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $idToken',
      },
      body: jsonEncode({'token': fcmToken}),
    );
  }

  /// 로그아웃 실행
  Future<void> handleLogout() async {
    await _authService.signOut();
    setState(() {
      _currentUser = null;
    });
    Navigator.pushReplacementNamed(context, RouteName.login);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[200], // ✅ 배경색 설정
      appBar: AppBar(
        title: const Text("로그인", style: TextStyle(fontWeight: FontWeight.bold)),
        centerTitle: true,
        elevation: 0,
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              // ✅ 로그인 UI
              if (_currentUser == null) _buildLoginButton() else _buildUserProfile(),
            ],
          ),
        ),
      ),
    );
  }

  /// Google 로그인 버튼 UI
  Widget _buildLoginButton() {
    return Card(
      elevation: 5,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            const Text(
              "환영합니다!\nGoogle 계정으로 로그인해주세요.",
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.w500),
            ),
            const SizedBox(height: 20),
            OutlinedButton.icon(
              onPressed: handleGoogleLogin,
              icon: Image.asset('assets/google_logo.png', height: 24),
              label: const Text("Google 로그인", style: TextStyle(fontSize: 16)),
              style: OutlinedButton.styleFrom(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                side: const BorderSide(color: Colors.blue, width: 1.5),
              ),
            ),
          ],
        ),
      ),
    );
  }

  /// 로그인 후 사용자 정보 UI
  Widget _buildUserProfile() {
    return Card(
      elevation: 5,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            CircleAvatar(
              backgroundImage: NetworkImage(_currentUser!.photoURL ?? "https://via.placeholder.com/150"),
              radius: 40,
            ),
            const SizedBox(height: 12),
            Text(
              "안녕하세요, ${_currentUser!.displayName}님!",
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text(
              _currentUser!.email ?? "이메일 없음",
              style: const TextStyle(fontSize: 14, color: Colors.grey),
            ),
            const SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: handleLogout,
              icon: const Icon(Icons.logout),
              label: const Text("로그아웃"),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.redAccent,
                padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
