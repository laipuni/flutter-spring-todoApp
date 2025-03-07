import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';


class PushNotification{
  static final _firebaseMessaging = FirebaseMessaging.instance;
  static final FlutterLocalNotificationsPlugin _flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();
  static String? _token;
  
  static Future init() async{
    await _firebaseMessaging.requestPermission(
      alert: true,
      announcement: false, // 불필요한 옵션 제거
      badge: true,
      carPlay: false,
      criticalAlert: false,
      provisional: false,
      sound: true,
    );

    _token = await _firebaseMessaging.getToken();
    return _token;
  }

  static Future<void> initLocal() async {
    // 알림 권한 요청
    FirebaseMessaging messaging = FirebaseMessaging.instance;
    NotificationSettings settings = await messaging.requestPermission(
      alert: true,
      badge: true,
      sound: true,
    );

    // 안드로이드 알림 채널 설정
    const AndroidInitializationSettings androidInitSettings =
    AndroidInitializationSettings('@mipmap/ic_launcher');

    const InitializationSettings initSettings = InitializationSettings(
      android: androidInitSettings,
    );

    await _flutterLocalNotificationsPlugin.initialize(initSettings);
  }

  static Future<void> showNotification(RemoteMessage message) async {
    const AndroidNotificationDetails androidDetails = AndroidNotificationDetails(
      'high_importance_channel', // 채널 ID
      'foreground_notification_channel', // 채널 이름
      importance: Importance.max,
      priority: Priority.high,
      showWhen: false,
      icon: 'mipmap/ic_launcher',
    );

    const NotificationDetails platformDetails =
    NotificationDetails(android: androidDetails);

    int notificationId = DateTime.now().millisecondsSinceEpoch ~/ 1000;

    await _flutterLocalNotificationsPlugin.show(
      notificationId,
      message.notification?.title ?? '알림',
      message.notification?.body ?? '내용 없음',
      platformDetails,
    );
  }
}