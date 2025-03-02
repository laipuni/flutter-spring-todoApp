import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class HttpInterceptor {
  final BuildContext context;

  HttpInterceptor(this.context);

  Future<Map<String, dynamic>> get(Uri url, {Map<String, String>? headers}) async {
    final response = await http.get(url, headers: headers);
    return _handleResponse(response);
  }

  Future<Map<String, dynamic>> post(Uri url, {Map<String, String>? headers, Object? body}) async {
    final response = await http.post(url, headers: headers, body: body);
    return _handleResponse(response);
  }

  Future<Map<String, dynamic>> put(Uri url,
  {Map<String, String>? headers, Object? body}) async {
    final response = await http.put(url, headers: headers, body: body);
    return _handleResponse(response);
  }

  Future<Map<String, dynamic>> delete(Uri url, {Map<String, String>? headers, Object? body}) async {
    final response = await http.delete(url, headers: headers, body: body);
    return _handleResponse(response);
  }

  // 🔹 모든 응답을 처리하는 함수
  Map<String, dynamic> _handleResponse(http.Response response) {
    if (response.statusCode == 401) {
      Map<String, dynamic> responseBody = jsonDecode(response.body);
      if (responseBody["redirect"] == "/login") {
        // 🔹 인증되지 않은 요청이면 로그인 페이지로 이동
        Navigator.pushNamed(context, "/login");
      }
    }
    return json.decode(response.body);
  }
}