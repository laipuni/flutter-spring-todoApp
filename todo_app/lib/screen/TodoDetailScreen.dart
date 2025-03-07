import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:todo_app/HostName.dart';
import 'package:todo_app/RouteName.dart';
import 'package:todo_app/http/HttpInterceptor.dart';
import 'package:todo_app/model/Todo.dart';
import 'package:todo_app/service/SecureStorageService.dart';
import 'package:todo_app/util/DateTimeUtils.dart';

class TodoDetailScreen extends StatefulWidget {
  const TodoDetailScreen({super.key});

  @override
  State<TodoDetailScreen> createState() => _TodoDetailScreenState();
}

class _TodoDetailScreenState extends State<TodoDetailScreen> {
  TodoDetail? todoDetail;
  final SecureStorageService secureStorageService = SecureStorageService();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      int todoId = ModalRoute.of(context)!.settings.arguments as int;
      receiveTodoDetail(todoId);
    });
  }

  Future<void> receiveTodoDetail(int todoId) async {
    var url = Uri.parse("${HostName.host}/api/todos/$todoId");
    String? idToken = await secureStorageService.getAccessToken();

    Map<String, dynamic> response = await HttpInterceptor(context).get(
      url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $idToken',
      },
    );

    setState(() {
      todoDetail = TodoDetail.fromJson(response["data"]);
    });
  }

  Future<void> removeTodoDetail() async{
    var url = Uri.parse("${HostName.host}/api/todos");
    String? idToken = await secureStorageService.getAccessToken();

    Map<String, dynamic> body = {
      "todoId" : todoDetail!.id
    };

    Map<String, dynamic> response = await HttpInterceptor(context).delete(
      url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $idToken',
      },
      body: jsonEncode(body)
    );

    if (response["code"]?.toString() == "200") {
      Navigator.pushReplacementNamed(context, RouteName.home);
    }
  }

  @override
  Widget build(BuildContext context) {
    if (todoDetail == null) {
      return Scaffold(
        appBar: AppBar(title: Center(child: Text("할일 상세"))),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: Text("할일 상세", style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
        actions: [
          IconButton(onPressed: () => {
              Navigator.pushReplacementNamed(context, RouteName.home)},
              icon: Icon(Icons.home))
        ],
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildInfoCard(Icons.title, "제목", todoDetail!.title),
            _buildInfoCard(Icons.description, "내용", todoDetail!.description),
            _buildInfoCard(Icons.date_range, "기간",
                "${DateTimeUtils.showDateTime(todoDetail!.startTime)} ~ ${DateTimeUtils.showDateTime(todoDetail!.dueTime)}"),
            _buildInfoCard(Icons.assignment_turned_in, "상태", todoDetail!.status.label),
            _buildInfoCard(Icons.priority_high, "중요도", todoDetail!.priority.label),
            _buildInfoCard(Icons.notifications, "알림 타입", todoDetail!.timeType.label),
            SizedBox(height: 20),
            Center(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  ElevatedButton.icon(
                    onPressed: () => removeTodoDetail(),
                    icon: Icon(Icons.delete, size: 18),
                    label: Text("제거하기", style: TextStyle(fontSize: 18)),
                    style: ElevatedButton.styleFrom(
                      padding: EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                      backgroundColor: Colors.redAccent,
                      foregroundColor: Colors.white,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                  ),
                  ElevatedButton.icon(
                    onPressed: () => {Navigator.pushReplacementNamed(context, RouteName.todoUpdate,arguments: todoDetail)},
                    icon: Icon(Icons.edit, size: 18),
                    label: Text("수정하기", style: TextStyle(fontSize: 18)),
                    style: ElevatedButton.styleFrom(
                      padding: EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                      backgroundColor: Colors.blueAccent,
                      foregroundColor: Colors.white,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                  ),
                ],
              )
            ),
          ],
        ),
      ),
    );
  }


  Widget _buildInfoCard(IconData icon, String title, String value) {
    return Card(
      elevation: 3,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      margin: EdgeInsets.symmetric(vertical: 8),
      child: Padding(
        padding: EdgeInsets.all(16.0),
        child: Row(
          children: [
            Icon(icon, size: 28, color: Colors.blueAccent),
            SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(title, style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.black54)),
                  SizedBox(height: 4),
                  Text(value, style: TextStyle(fontSize: 18, fontWeight: FontWeight.w600)),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
