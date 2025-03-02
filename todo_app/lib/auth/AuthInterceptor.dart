import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:todo_app/service/SecureStorageService.dart';
import 'package:todo_app/service/SharedPreferencesService.dart';

class AuthInterceptor extends StatefulWidget {
  final Widget child;
  final String routeName;// 현재 페이지의 라우트 이름
  const AuthInterceptor({required this.child, required this.routeName, super.key});

  @override
  State<AuthInterceptor> createState() => _AuthInterceptorState();
}

class _AuthInterceptorState extends State<AuthInterceptor> {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<bool>(
      future: isAuthenticated(),
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return const Center(child: CircularProgressIndicator()); //  인증 확인
        }

        if (snapshot.data == false) {
          //  인증되지 않은 사용자는 로그인 페이지로 이동
          SharedPreferencesService().saveRedirectUrl(widget.routeName);
          WidgetsBinding.instance.addPostFrameCallback((_) {
            Navigator.pushReplacementNamed(context, "/login");
          });
          return const SizedBox(); // 빈 화면 반환
        }

        return widget.child; //  인증된 경우, 원래 화면 표시
      },
    );
  }

  //  SharedPreferencesService를 사용하여 로그인 여부 확인
  Future<bool> isAuthenticated() async {
    String? token = await SecureStorageService().getAccessToken();
    return token != null;
  }
}