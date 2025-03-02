import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class SecureStorageService {
  static final String ACCESS_TOKEN = "access_token";
  static final String REFRESH_TOKEN = "refresh_token";

  static final SecureStorageService _instance = SecureStorageService._internal();
  final FlutterSecureStorage _storage = const FlutterSecureStorage();

  factory SecureStorageService() {
    return _instance;
  }

  SecureStorageService._internal();

  // 토큰 저장 (암호화됨)
  Future<void> saveAccessToken(String token) async {
    await _storage.write(key: ACCESS_TOKEN, value: token);
  }

  // 토큰 가져오기
  Future<String?> getAccessToken() async {
    return await _storage.read(key: ACCESS_TOKEN);
  }

  // refresh_token 저장
  Future<void> saveRefreshToken(String token) async {
    await _storage.write(key: REFRESH_TOKEN, value: token);
  }

  // refresh_token 가져오기
  Future<String?> getRefreshToken() async {
    return await _storage.read(key: REFRESH_TOKEN);
  }

  // 토큰 삭제
  Future<void> clearTokens() async {
    await _storage.delete(key: ACCESS_TOKEN);
    await _storage.delete(key: REFRESH_TOKEN);
  }
}
