

import 'package:todo_app/enum/ReminderTime.dart';
import 'package:todo_app/enum/TodoPriority.dart';
import 'package:todo_app/enum/TodoStatus.dart';

class TodoView{
  int id;
  String title;
  DateTime startTime;
  DateTime dueTime;
  TodoStatus status;
  TodoPriority priority;

  TodoView({
     required this.id,
     required this.title,
     required this.startTime,
     required this.dueTime,
     required this.status,
     required this.priority,
   });

   factory TodoView.fromJson(Map<String, dynamic> json) {
     return TodoView(
       id: json['id'],
       title: json['title'],
       startTime: DateTime.parse(json['startDate']),
       dueTime: DateTime.parse(json['dueDate']),
       status: TodoStatus.fromValue(json['status']),
       priority: TodoPriority.fromValue(json['priority']),
     );
   }
}

class TodoDetail{
  int id;
  String title;
  String description;
  DateTime startTime;
  DateTime dueTime;
  TodoStatus status;
  TodoPriority priority;
  ReminderTime timeType;

  TodoDetail({
    required this.id,
    required this.title,
    required this.description,
    required this.startTime,
    required this.dueTime,
    required this.status,
    required this.priority,
    required this.timeType
  });

  factory TodoDetail.fromJson(Map<String, dynamic> json) {
    return TodoDetail(
      id: json['id'],
      title: json['title'],
      description: json['description'] ?? "없음",
      startTime: DateTime.parse(json['startDate']),
      dueTime: DateTime.parse(json['dueDate']),
      status: TodoStatus.fromValue(json['status']),
      priority: TodoPriority.fromValue(json['priority']),
      timeType: ReminderTime.fromValue(json['timeType'])
    );
  }
}