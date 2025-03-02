import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_datetime_picker_plus/flutter_datetime_picker_plus.dart';
import 'package:todo_app/HostName.dart';
import 'package:todo_app/RouteName.dart';
import 'package:todo_app/enum/ReminderTime.dart';
import 'package:todo_app/enum/TodoPriority.dart';
import 'package:todo_app/enum/TodoStatus.dart';
import 'package:todo_app/http/HttpInterceptor.dart';
import 'package:todo_app/service/SecureStorageService.dart';
import 'package:todo_app/util/DateTimeUtils.dart';

class BoardAddScreen extends StatefulWidget {
  const BoardAddScreen({super.key});

  @override
  State<BoardAddScreen> createState() => _BoardAddScreenState();
}

class _BoardAddScreenState extends State<BoardAddScreen> {
  final SecureStorageService secureStorageService = SecureStorageService();
  final TextEditingController titleController = TextEditingController();
  final TextEditingController descriptionController = TextEditingController();

  ReminderTime _selectedTime = ReminderTime.none;
  TodoStatus _selectedStatus = TodoStatus.todo;
  TodoPriority _selectedPriority = TodoPriority.LOW;

  DateTime selectedStartDateTime = DateTime.now();
  DateTime selectedDueDateTime = DateTime.now();
  bool _isLoading = false; // 로딩 상태 추가

  void _selectDateTime(Function(DateTime) onDateSelected) {
    DatePicker.showDateTimePicker(
      context,
      showTitleActions: true,
      minTime: DateTime(2000, 1, 1),
      maxTime: DateTime(2100, 12, 31),
      onConfirm: onDateSelected,
      currentTime: DateTime.now(),
      locale: LocaleType.ko,
    );
  }

  void setStartDateTime(DateTime dateTime) {
    setState(() {
      selectedStartDateTime = dateTime;
    });
  }

  void setDueDateTime(DateTime dateTime) {
    setState(() {
      selectedDueDateTime = dateTime;
    });
  }

  Future<void> saveTodo() async {
    setState(() => _isLoading = true);

    var url = Uri.parse("${HostName.host}/api/todos");
    String? idToken = await secureStorageService.getAccessToken();

    var data = {
      "title": titleController.text,
      "description": descriptionController.text,
      "startDate": selectedStartDateTime.toIso8601String(),
      "dueDate": selectedDueDateTime.toIso8601String(),
      "priority": _selectedPriority.value,
      "status": _selectedStatus.value,
      "timeType": _selectedTime.value
    };

    Map<String, dynamic> response = await HttpInterceptor(context).post(
      url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $idToken',
      },
      body: jsonEncode(data),
    );

    setState(() => _isLoading = false);

    if (response["code"]?.toString() == "200") {
      Navigator.pushReplacementNamed(context, RouteName.home);
    } else if(response["code"]?.toString() == "400"){
      _showSnackBar(response["message"]?.toString());
    }
  }

  void _showSnackBar(String? message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message!), duration: Duration(seconds: 2)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("할 일 추가", style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.blueAccent,
        foregroundColor: Colors.white,
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator()) // 🔄 로딩 중 표시
          : SingleChildScrollView(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildTextField(titleController, "제목"),
            SizedBox(height: 16),
            _buildTextField(descriptionController, "내용"),
            SizedBox(height: 16),
            _buildDatePicker("시작 시간", selectedStartDateTime, setStartDateTime),
            _buildDatePicker("마감 시간", selectedDueDateTime, setDueDateTime),
            _buildDivider(),
            _buildRadioSection("알림 설정", getReminderTimeRadio(), _selectedTime.label),
            _buildDivider(),
            _buildRadioSection("상태 설정", getStatusRadio(), _selectedStatus.label),
            _buildDivider(),
            _buildRadioSection("중요도 설정", getPriorityRadio(), _selectedPriority.label),
            _buildDivider(),
            _buildActionButtons(),
          ],
        ),
      ),
    );
  }

  Widget _buildTextField(TextEditingController controller, String label) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
        border: OutlineInputBorder(),
      ),
    );
  }

  Widget _buildDatePicker(String label, DateTime date, Function(DateTime) onDateSelected) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Text("$label: ${DateTimeUtils.showDateTime(date)}", style: TextStyle(fontWeight: FontWeight.bold)),
            SizedBox(height: 8),
            ElevatedButton(
              onPressed: () => _selectDateTime(onDateSelected),
              child: Text("$label 선택"),
            ),
          ],
        )
      ],
    );
  }

  Widget _buildRadioSection(String title, List<Widget> radioList, String selectedValue) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(title, style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
        Column(children: radioList),
      ],
    );
  }

  Widget _buildDivider() {
    return Divider(color: Colors.grey, thickness: 1.5, height: 24);
  }

  Widget _buildActionButtons() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        TextButton(
          onPressed: () => Navigator.pushReplacementNamed(context, RouteName.home),
          child: Text("취소", style: TextStyle(fontSize: 16)),
        ),
        ElevatedButton(
          onPressed: saveTodo,
          style: ElevatedButton.styleFrom(backgroundColor: Colors.blueAccent),
          child: Text("저장", style: TextStyle(fontSize: 16, color: Colors.white)),
        ),
      ],
    );
  }

  List<Widget> getReminderTimeRadio() {
    return ReminderTime.values
        .map((time) => RadioListTile<ReminderTime>(
      title: Text(time.label),
      value: time,
      groupValue: _selectedTime,
      onChanged: (value) => setState(() => _selectedTime = value!),
    ))
        .toList();
  }

  List<Widget> getStatusRadio() {
    return TodoStatus.values
        .map((status) => RadioListTile<TodoStatus>(
      title: Text(status.label),
      value: status,
      groupValue: _selectedStatus,
      onChanged: (value) => setState(() => _selectedStatus = value!),
    ))
        .toList();
  }

  List<Widget> getPriorityRadio() {
    return TodoPriority.values
        .map((priority) => RadioListTile<TodoPriority>(
      title: Text(priority.label),
      value: priority,
      groupValue: _selectedPriority,
      onChanged: (value) => setState(() => _selectedPriority = value!),
    ))
        .toList();
  }
}
