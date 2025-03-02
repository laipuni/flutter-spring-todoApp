enum TodoPriority{
  LOW("낮음","low"),
  MEDIUM("중간","medium"),
  HIGH("높음","high");

  final String label;
  final String value;

  const TodoPriority(this.label, this.value);

  // todoPriorityMap을 static으로 선언하고, 초기화
  static final Map<String, TodoPriority> todoPriorityMap =
  Map.fromEntries(TodoPriority.values.map((e) => MapEntry(e.value, e)));

  // String 값을 TodoPriority enum으로 변환하는 메서드
  static TodoPriority fromValue(String value) {
    return todoPriorityMap[value.toLowerCase()] ?? TodoPriority.LOW; // 기본값 LOW
  }
}