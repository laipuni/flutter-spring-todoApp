import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:todo_app/RouteName.dart';

class HttpInterceptor {
  final BuildContext context;

  HttpInterceptor(this.context);

  Future<T> _executeWithErrorHandling<T>(Future<T> Function() httpCall) async {
    try {
      return await httpCall();
    } catch (e) {
      if (e is SocketException) {
        Navigator.pushReplacementNamed(context,RouteName.serverError);
      }
      rethrow; // 다른 예외는 상위로 전달
    }
  }

  Future<Map<String, dynamic>> get(Uri url, {Map<String, String>? headers}) async {
    return _executeWithErrorHandling(() async {
      final response = await http.get(url, headers: headers);
      return _handleResponse(response);
    });
  }

  Future<Map<String, dynamic>> post(Uri url, {Map<String, String>? headers, Object? body}) async {
    return _executeWithErrorHandling(() async {
      final response = await http.post(url, headers: headers, body: body);
      return _handleResponse(response);
    });
  }

  Future<Map<String, dynamic>> put(Uri url,
  {Map<String, String>? headers, Object? body}) async {
    return _executeWithErrorHandling(() async {
      final response = await http.put(url, headers: headers, body: body);
      return _handleResponse(response);
    });
  }

  Future<Map<String, dynamic>> delete(Uri url, {Map<String, String>? headers, Object? body}) async {
    return _executeWithErrorHandling(() async {
      final response = await http.delete(url, headers: headers, body: body);
      return _handleResponse(response);
    });
  }

  void _showSnackBar(String? message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message!), duration: Duration(seconds: 2)),
    );
  }

  // 🔹 모든 응답을 처리하는 함수
  Map<String, dynamic> _handleResponse(http.Response response) {
    handleError(response);
    if(response.body.isNotEmpty){
      return json.decode(response.body);
    }
    return {};
  }

  void handleError(http.Response response) {
    if(response.statusCode == 400){
      if(response.body.isEmpty){
        _showSnackBar("잘못된 입력을 작성하셨습니다.");
      }
      Map<String,dynamic> responseMap = json.decode(response.body);
      _showSnackBar(responseMap["message"]?.toString());
    }
    else if (response.statusCode == 403 || response.statusCode == 401) {
      //인증되지 않은 요청이면 로그인 페이지로 이동
      Navigator.pushReplacementNamed(context, RouteName.login);
    } else if(response.statusCode > 400 && response.statusCode < 500){
      //클라이언트 에러 페이지로 향한다.
      Navigator.pushReplacementNamed(context, RouteName.clientError);
    } else if(500 <= response.statusCode){
      //서버 에러 페이지로 향한다.
      Navigator.pushReplacementNamed(context, RouteName.serverError);
    }
  }
}