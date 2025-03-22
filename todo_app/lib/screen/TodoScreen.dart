import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:todo_app/HostName.dart';
import 'package:todo_app/RouteName.dart';
import 'package:todo_app/auth/AuthService.dart';
import 'package:todo_app/enum/Sort.dart';
import 'package:todo_app/enum/TodoPriority.dart';
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
  final ScrollController _scrollController = ScrollController();

  int page = 0;
  bool hasNext = true;
  final TextEditingController search = TextEditingController();
  Sort order = TodoSort.latest;
  Sort sort = Direction.DESC;

  @override
  void initState() {
    super.initState();
    setTodoList();
    _scrollController.addListener(scrollListener);
  }

  void scrollListener(){
    if (isEndScroll() && !_isLoading && hasNext) {
      //다음 스크롤에 정보가 있다면 불러오기
      addNextTodoList();
    }
  }

  bool isEndScroll() {
    //스크롤이 화면에 하단으로 내려갔는지 확인
    return _scrollController.position.pixels >=
      _scrollController.position.maxScrollExtent * 0.90;
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  Future<void> setTodoList() async {
    setState(() {
      _isLoading = true;
    });
    page = 0;
    Map<String, dynamic> response = await receiveTodoList();
    List<dynamic> todosJson = response["data"]["todoList"];
    setState(() {
      _todoList = todosJson.map((todo) => TodoView.fromJson(todo)).toList();
      hasNext = response["data"]["hasNext"];
      _isLoading = false;
    });
  }

  Future<void> addNextTodoList() async{
    setState(() {
      _isLoading = true;
    });
    // 다음 페이지의 정보를 불러옴
    page += 1;
    Map<String, dynamic> response = await receiveTodoList();
    List<dynamic> todosJson = response["data"]["todoList"];
    final newItems = todosJson.map((todo) => TodoView.fromJson(todo)).toList();
    setState(() {
      hasNext = response["data"]["hasNext"];
      _todoList.addAll(newItems);
      _isLoading = false;
    });
  }

  Future<Map<String, dynamic>> receiveTodoList() async{
    var url = Uri.parse("${HostName.host}/api/v2/todos?page=$page&search=${search.text}&order=${order.value}&sort=${sort.value}");
    String? idToken = await _secureStorageService.getAccessToken();

    Map<String, dynamic> response = await HttpInterceptor(context).get(
      url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $idToken',
      },
    );
    return response;
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
      body:
          _todoList.isEmpty
          ? Column(
        children: [
          SizedBox(height: 20), // 검색바 위에 공백 추가
          _buildSearchBar(),
          SizedBox(height: 30), // 검색바 아래 공백 추가
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              bulidDropdownButton(TodoSort.values,order,setOrder),
              bulidDropdownButton(Direction.values,sort,setSort),
            ],
          ),
          Expanded(
            child: Center(
              child: Text(
                "등록된 할 일이 없습니다.",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
            ),
          ),
        ],
      )
          : Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              SizedBox(height: 20), // 검색바 위에 공백 추가
              _buildSearchBar(),
              SizedBox(height: 10), // 검색바 위에 공백 추가
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  bulidDropdownButton(TodoSort.values,order,setOrder),
                  bulidDropdownButton(Direction.values,sort,setSort),
                ],
              ),
              SizedBox(height: 30), // 검색바 아래 공백 추가
              Expanded(
                child: ListView.builder(
                  controller: _scrollController,
                  itemCount: _todoList.length,
                  itemBuilder: (context, index) {
                    return _buildTodoCard(_todoList[index]);
                  },
                ),
              ),
              if (_isLoading)
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 40),
                  child: CircularProgressIndicator(
                    color: Colors.blueAccent,
                  ),
                ),
            ],
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
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            _buildPriorityBadge(todo.priority),
            Icon(Icons.arrow_forward_ios, color: Colors.grey[600]),
          ],
        ),
        onTap: () {
          Navigator.pushReplacementNamed(context, RouteName.todoDetail, arguments: todo.id);
        },
      ),
    );
  }

  Widget _buildPriorityBadge(TodoPriority priority) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: _getPriorityColor(priority),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Text(
        priority.label,
        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
      ),
    );
  }

  MaterialColor _getPriorityColor(TodoPriority priority){
    if(priority == TodoPriority.LOW){
      return Colors.green;
    } else if(priority == TodoPriority.MEDIUM){
      return Colors.orange;
    } else {
      return Colors.red;
    }
  }

  Widget _buildSearchBar(){
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 20), // 검색바 좌우 여백
      child: TextField(
        controller: search,
        decoration: InputDecoration(
          hintText: '검색어를 입력하세요',
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          suffixIcon: IconButton(
            icon: Icon(Icons.search), // 돋보기 모양 아이콘
            onPressed: () {
              setTodoList(); // 돋보기 버튼 클릭 시 실행할 함수
            },
          ),
        ),
      ),
    );
  }

  Widget bulidDropdownButton(List<Sort> sort, Sort value, ValueChanged<Sort?>? callback){
    List<DropdownMenuItem<Sort>> items = getSortDropdownMenuItem(sort);
    return DropdownButton<Sort>(
        value: value, // 선택된 값
        items: items,
        onChanged: (Sort? newValue) => {callback?.call(newValue),setTodoList()},
      );
  }

  List<DropdownMenuItem<Sort>> getSortDropdownMenuItem(List<Sort> sort){
    return sort.map((sort) {
      return DropdownMenuItem<Sort>(
        value: sort,
        child: Text(sort.label),
      );
    }).toList();
  }

  void setOrder(Sort? value) {
    if (value == null) return;
    setState(() {
      order = value;
    });
  }

  void setSort(Sort? value) {
    if (value == null) return;
    setState(() {
      sort = value;
    });
  }
}
