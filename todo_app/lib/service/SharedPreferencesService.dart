import 'package:shared_preferences/shared_preferences.dart';

class SharedPreferencesService {
  static final String FCM_TOKEN = "fcm_token";
  static final String REDIRECT_URL = "redirect_url";

  static final SharedPreferencesService _instance = SharedPreferencesService._internal();
  SharedPreferences? _prefs;

  factory SharedPreferencesService() {
    return _instance;
  }

  SharedPreferencesService._internal();

  // 🔹 초기화 (앱 시작 시 한 번 실행 필요)
  Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  // 🔹 로그인 시 access_token 저장
  Future<void> saveFcmToken(String token) async {
    await _prefs?.setString(FCM_TOKEN, token);
  }

  // 🔹 저장된 access_token 가져오기
  Future<String?> getFcmToken() async {
    return _prefs?.getString(FCM_TOKEN);
  }

  // 🔹 로그아웃 시 access_token 삭제
  Future<void> clearFcmToken() async {
    await _prefs?.remove(FCM_TOKEN);
  }

  Future<void> saveRedirectUrl(String redirectUrl) async{
    await _prefs?.setString(REDIRECT_URL, redirectUrl);
  }

  Future<String?> getRedirectUrl() async {
    return _prefs?.getString(REDIRECT_URL);
  }

  // 🔹 로그아웃 시 access_token 삭제
  Future<void> clearRedirectUrl() async {
    await _prefs?.remove(REDIRECT_URL);
  }
}
