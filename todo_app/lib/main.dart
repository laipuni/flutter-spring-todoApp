import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: BoardScreen(),
    );
  }
}

class BoardScreen extends StatefulWidget {
  @override
  _BoardScreenState createState() => _BoardScreenState();
}

class _BoardScreenState extends State<BoardScreen> {
  final List<Map<String, String>> _posts = []; // 게시글 목록
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _contentController = TextEditingController();
  String serverMessage = "서버 응답 없음"; // 서버에서 받아온 데이터

  void _addPost() {
    String title = _titleController.text.trim();
    String content = _contentController.text.trim();

    if (title.isNotEmpty && content.isNotEmpty) {
      setState(() {
        _posts.add({"title": title, "content": content});
      });
      _titleController.clear();
      _contentController.clear();
      Navigator.of(context).pop();
    }
  }

  void _showAddPostDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text("새 게시글 추가"),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: _titleController,
                decoration: InputDecoration(labelText: "제목"),
              ),
              TextField(
                controller: _contentController,
                decoration: InputDecoration(labelText: "내용"),
                maxLines: 3,
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: Text("취소"),
            ),
            TextButton(
              onPressed: _addPost,
              child: Text("추가"),
            ),
          ],
        );
      },
    );
  }

  // 🌐 API 요청 함수 추가
  Future<void> _fetchServerData() async {
    //Android 에뮬레이터에서 localhost를 사용하면 안 됨
    //10.0.2.2는 에뮬레이터에서 Host PC (Spring Boot 서버)로의 주소
    const String apiUrl = "http://10.0.2.2:8080/api/flutter-listTest"; // Android 에뮬레이터
    try {
      final response = await http.get(Uri.parse(apiUrl));
      if (response.statusCode == 200) {
        List<dynamic> jsonData = jsonDecode(response.body); // JSON 리스트 디코딩

        setState(() {
          serverMessage = "서버 정상 응답!";
          _posts.addAll(
              jsonData.map((data) => {"title": data["title"], "content": data["content"]})
          );
        });
      } else {
        setState(() {
          serverMessage = "서버 오류: ${response.statusCode}";
        });
      }
    } catch (e) {
      setState(() {
        serverMessage = "네트워크 오류: $e";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("게시판")),
      body: Column(
        children: [
          // 🌐 서버에서 받아온 메시지 표시
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text("서버 응답: $serverMessage", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          ),

          // 📜 게시글 목록
          Expanded(
            child: _posts.isEmpty
                ? Center(child: Text("게시글이 없습니다."))
                : ListView.builder(
              itemCount: _posts.length,
              itemBuilder: (context, index) {
                return Card(
                  margin: EdgeInsets.all(8),
                  child: ListTile(
                    title: Text(_posts[index]["title"]!),
                    subtitle: Text(_posts[index]["content"]!),
                  ),
                );
              },
            ),
          ),

          // 🌐 API 호출 버튼 추가
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: ElevatedButton(
              onPressed: _fetchServerData,
              child: Text("서버에서 데이터 가져오기"),
            ),
          ),
        ],
      ),

      floatingActionButton: FloatingActionButton(
        onPressed: _showAddPostDialog,
        child: Icon(Icons.add),
      ),
    );
  }
}
