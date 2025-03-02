import 'package:flutter/cupertino.dart';
import 'package:todo_app/auth/AuthInterceptor.dart';
import 'package:todo_app/screen/BoardAddScreen.dart';
import 'package:todo_app/screen/BoardDetailScreen.dart';
import 'package:todo_app/screen/BoardScreen.dart';
import 'package:todo_app/screen/BoardUpdateScreen.dart';
import 'package:todo_app/screen/LoginScreen.dart';

class RouteName {
  static const home = "/";
  static const login = "/login";
  static const todoDetail = "/detail-todo";
  static const todoUpdate = "/update-todo";
  static const todoAdd = "/add-todo";
}

var routeName = {
  RouteName.home : (context) => AuthInterceptor(routeName: "/", child: BoardScreen()),
  RouteName.todoAdd : (context) => AuthInterceptor(routeName: "/add-todo", child: BoardAddScreen()),
  RouteName.todoDetail : (context) => AuthInterceptor(routeName: "/detail-todo", child: BoardDetailScreen()),
  RouteName.todoUpdate : (context) => AuthInterceptor(routeName: "/update-tod",child: BoardUpdateScreen()),
  RouteName.login : (context) => LoginScreen(),
};