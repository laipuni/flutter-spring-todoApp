import 'dart:convert';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:todo_app/auth/AuthService.dart';
import 'package:todo_app/fcm/PushNotification.dart';

final navigatorKey = GlobalKey<NavigatorState>();
String? _token;

@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  if (message.notification != null) {
    print("Notification Received, msg = ${message.notification}");
  }
}

Future<void> setupInteractedMessage() async {
  RemoteMessage? initialMessage = await FirebaseMessaging.instance.getInitialMessage();

  // 앱을 껐다 켰을 경우
  if (initialMessage != null) {
    _handleMessage(initialMessage);
  }

  // 앱이 백그라운드 상태에서 사용자가 알림을 탭할 때 핸들러 지정
  FirebaseMessaging.onMessageOpenedApp.listen(_handleMessage);
}

void _handleMessage(RemoteMessage message) {
  Future.delayed(const Duration(seconds: 1), () {
    navigatorKey.currentState!.pushNamed("/message", arguments: message);
  });
}

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  _token = await PushNotification.init();
  print("파이어 베이스 토큰: $_token");
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  setupInteractedMessage();

  // Foreground 푸시 알림 처리
  FirebaseMessaging.onMessage.listen((RemoteMessage message) {
    if (message.notification != null) {
      PushNotification.showNotification(message);
    }
  });

  sendToken(_token);

  runApp(_MyApp());
}

Future<void> sendToken(token) async {
  const String apiUrl = "http://10.0.2.2:8080/send"; // Android 에뮬레이터
  try {
    http.post(
      Uri.parse(apiUrl),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({'token': token}),
    );
  } catch (e) {
    print("네트워크 오류 발생: $e");
  }
}

class _MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      navigatorKey: navigatorKey,
      debugShowCheckedModeBanner: false,
      home: _BoardScreen(),
    );
  }
}

class _BoardScreen extends StatefulWidget {
  @override
  _BoardScreenState createState() => _BoardScreenState();
}

class _BoardScreenState extends State<_BoardScreen> {
  final List<Map<String, String>> _posts = []; // 게시글 목록
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _contentController = TextEditingController();
  String serverMessage = "서버 응답 없음"; // 서버에서 받아온 데이터
  final AuthService _authService = AuthService();
  User? _currentUser;

  /// 🔹 Google 로그인 실행
  Future<void> _handleGoogleLogin() async {
    User? user = await _authService.signInWithGoogle();
    if (user != null) {
      String? idToken = await user.getIdToken();
      await _authService.sendTokenToBackend(idToken, _token);
      setState(() {
        _currentUser = user;
      });
    }
  }

  /// 🔹 로그아웃 실행
  Future<void> _handleLogout() async {
    await _authService.signOut();
    setState(() {
      _currentUser = null;
    });
  }

  void _addPost() {
    String title = _titleController.text.trim();
    String content = _contentController.text.trim();

    if (title.isNotEmpty && content.isNotEmpty) {
      setState(() {
        _posts.add({"title": title, "content": content});
      });
      _titleController.clear();
      _contentController.clear();
      Navigator.of(context).pop();
    }
  }

  void _showAddPostDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text("새 게시글 추가"),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: _titleController,
                decoration: InputDecoration(labelText: "제목"),
              ),
              TextField(
                controller: _contentController,
                decoration: InputDecoration(labelText: "내용"),
                maxLines: 3,
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: Text("취소"),
            ),
            TextButton(
              onPressed: _addPost,
              child: Text("추가"),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("게시판")),
      body: Column(
        children: [
          // 🌐 Google 로그인 버튼 추가
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: _currentUser == null
                ? ElevatedButton.icon(
              icon: Icon(Icons.login),
              label: Text("Google 로그인"),
              onPressed: _handleGoogleLogin,
            )
                : Column(
              children: [
                Text("환영합니다, ${_currentUser!.displayName}님"),
                SizedBox(height: 10),
                ElevatedButton.icon(
                  icon: Icon(Icons.logout),
                  label: Text("로그아웃"),
                  onPressed: _handleLogout,
                ),
              ],
            ),
          ),

          // 🌐 서버에서 받아온 메시지 표시
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text("서버 응답: $serverMessage",
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          ),

          // 📜 게시글 목록
          Expanded(
            child: _posts.isEmpty
                ? Center(child: Text("게시글이 없습니다."))
                : ListView.builder(
              itemCount: _posts.length,
              itemBuilder: (context, index) {
                return Card(
                  margin: EdgeInsets.all(8),
                  child: ListTile(
                    title: Text(_posts[index]["title"]!),
                    subtitle: Text(_posts[index]["content"]!),
                  ),
                );
              },
            ),
          ),

          // 🌐 API 호출 버튼 추가
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: ElevatedButton(
              onPressed: () => sendToken(_token),
              child: Text("서버에서 데이터 가져오기"),
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _showAddPostDialog,
        child: Icon(Icons.add),
      ),
    );
  }

}
