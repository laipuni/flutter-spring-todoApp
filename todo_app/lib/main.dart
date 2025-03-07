import 'dart:convert';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:todo_app/RouteName.dart';
import 'package:todo_app/fcm/PushNotification.dart';
import 'package:todo_app/service/SharedPreferencesService.dart';

final navigatorKey = GlobalKey<NavigatorState>();
final SharedPreferencesService _sharedPreferencesService = SharedPreferencesService();

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
    print("Notification Received, msg = ${message.notification}");
  });
}

void handlePushNotification(RemoteMessage message) {
  if (message.notification != null) {
    PushNotification.showNotification(message);
  }
}

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  await saveFcmToken();
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  setupInteractedMessage();
  // Foreground 푸시 알림 처리
  FirebaseMessaging.onMessage.listen(handlePushNotification);
  runApp(_MyApp());
}

Future<void> saveFcmToken() async {
  _sharedPreferencesService.init();
  String token = await PushNotification.init();
  _sharedPreferencesService.saveFcmToken(token);
}

class _MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      routes: routeName,
      initialRoute: RouteName.home,
    );
  }
}

