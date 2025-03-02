enum ReminderTime {
  none("설정 없음", "none"),
  five("5분 전","five"),
  fifteen("15분 전", "fifteen"),
  thirty("30분 전", "half"),
  oneHour("1시간 전", "hour");

  final String label; // UI에서 사용할 라벨
  final String value; // 서버 또는 데이터 저장 시 사용할 값
  static final Map<String, ReminderTime> reminderTimeMap =
  Map.fromEntries(ReminderTime.values.map((e) => MapEntry(e.value, e)));

  const ReminderTime(this.label, this.value);

  static ReminderTime fromValue(String value) {
    return reminderTimeMap[value.toLowerCase()] ?? ReminderTime.none; // 기본값 none
  }
}
