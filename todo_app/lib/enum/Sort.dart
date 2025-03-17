abstract class Sort{
  String get label;
  String get value;
  static List<Sort> get values => [];
}
enum TodoSort implements Sort{
  latest("최신순","latest"),
  priority("중요순","priority");

  @override
  final String label;
  @override
  final String value;

  const TodoSort(this.label, this.value);
}
enum Direction implements Sort{
  DESC("내림차순","desc"),
  ASC("오름차순","asc");

  @override
  final String label;
  @override
  final String value;

  const Direction(this.label,this.value);
}