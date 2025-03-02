import 'package:flutter/cupertino.dart';
import 'package:todo_app/auth/AuthInterceptor.dart';
import 'package:todo_app/screen/TodoAddScreen.dart';
import 'package:todo_app/screen/TodoDetailScreen.dart';
import 'package:todo_app/screen/TodoScreen.dart';
import 'package:todo_app/screen/TodoUpdateScreen.dart';
import 'package:todo_app/screen/LoginScreen.dart';

class RouteName {
  static const home = "/";
  static const login = "/login";
  static const todoDetail = "/detail-todo";
  static const todoUpdate = "/update-todo";
  static const todoAdd = "/add-todo";
}

var routeName = {
  RouteName.home : (context) => AuthInterceptor(routeName: "/", child: TodoScreen()),
  RouteName.todoAdd : (context) => AuthInterceptor(routeName: "/add-todo", child: TodoAddScreen()),
  RouteName.todoDetail : (context) => AuthInterceptor(routeName: "/detail-todo", child: TodoDetailScreen()),
  RouteName.todoUpdate : (context) => AuthInterceptor(routeName: "/update-tod",child: TodoUpdateScreen()),
  RouteName.login : (context) => LoginScreen(),
};