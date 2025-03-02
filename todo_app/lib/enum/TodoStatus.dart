enum TodoStatus {

  todo("시작전","todo"),
  inProgress("진행중","in_progress"),
  done("완료", "done");

  final String label;
  final String value;
  static final Map<String, TodoStatus> todoStatusMap =
  Map.fromEntries(TodoStatus.values.map((status) => MapEntry(status.value, status)));

  const TodoStatus (this.label, this.value);

  static TodoStatus fromValue(String value){
    return todoStatusMap[value.toLowerCase()] ?? TodoStatus.todo;
  }
}