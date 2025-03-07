import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:todo_app/HostName.dart';
import 'package:todo_app/RouteName.dart';
import 'package:todo_app/auth/AuthService.dart';
import 'package:todo_app/enum/TodoStatus.dart';
import 'package:todo_app/http/HttpInterceptor.dart';
import 'package:todo_app/model/Todo.dart';
import 'package:todo_app/service/SecureStorageService.dart';
import 'package:todo_app/util/DateTimeUtils.dart';

class TodoScreen extends StatefulWidget {
  @override
  _TodoScreenState createState() => _TodoScreenState();
}

class _TodoScreenState extends State<TodoScreen> {
  List<TodoView> _todoList = [];
  bool _isLoading = true;
  final SecureStorageService _secureStorageService = SecureStorageService();

  @override
  void initState() {
    super.initState();
    receiveTodoList();
  }

  Future<void> receiveTodoList() async {
    var url = Uri.parse("${HostName.host}/api/todos");
    String? idToken = await _secureStorageService.getAccessToken();

    Map<String, dynamic> response = await HttpInterceptor(context).get(
      url,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $idToken',
        },
    );
    setTodoList(response);
  }

  void setTodoList(Map<String, dynamic> response) {
    List<dynamic> todosJson = response["data"]["todoList"];

    setState(() {
      _todoList = todosJson.map((todo) => TodoView.fromJson(todo)).toList();
      _isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("할 일 목록", style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: Icon(Icons.logout),
            onPressed: () {
              AuthService().signOut();
              Navigator.pushReplacementNamed(context, RouteName.login);
            },
          ),
        ],
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : _todoList.isEmpty
          ? Center(child: Text("등록된 할 일이 없습니다.", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)))
          : Padding(
        padding: EdgeInsets.all(8.0),
        child: ListView.builder(
          itemCount: _todoList.length,
          itemBuilder: (context, index) {
            return _buildTodoCard(_todoList[index]);
          },
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => Navigator.pushReplacementNamed(context, RouteName.todoAdd),
        icon: Icon(Icons.add),
        label: Text("추가하기"),
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
      ),
    );
  }

  Widget buildTodoStatusIcon(TodoStatus status){
    if(status == TodoStatus.done){
      return Icon(Icons.check_circle_outline, color: Colors.green, size: 32);
    } else if (status == TodoStatus.inProgress){
      return Icon(Icons.circle_notifications_outlined, color: Colors.blueAccent, size: 32);
    } else {
      return Icon(Icons.circle_outlined, color: Colors.grey, size: 32);
    }
  }

  Widget _buildTodoCard(TodoView todo) {
    return Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      elevation: 4,
      margin: EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      child: ListTile(
        contentPadding: EdgeInsets.all(12),
        leading: buildTodoStatusIcon(todo.status),
        title: Text(todo.title, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
        subtitle: Text(
          "${DateTimeUtils.showDateTime(todo.startTime)} ~ ${DateTimeUtils.showDateTime(todo.dueTime)}",
          style: TextStyle(fontSize: 14, color: Colors.grey[700]),
        ),
        trailing: Icon(Icons.arrow_forward_ios, color: Colors.grey[600]),
        onTap: () {
          Navigator.pushReplacementNamed(context, RouteName.todoDetail, arguments: todo.id);
        },
      ),
    );
  }
}
