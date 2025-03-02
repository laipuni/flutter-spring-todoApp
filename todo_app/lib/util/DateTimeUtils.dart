class DateTimeUtils{

  static String showDateTime(DateTime dateTime){
    String year = dateTime.year.toString();
    String month = dateTime.month.toString();
    String day = dateTime.day.toString();
    String hour = dateTime.hour.toString();
    String minutes = dateTime.minute.toString();
    return "$year년$month월$day일 $hour시$minutes분";
  }

  static String showDateTimeShort(DateTime dateTime){
    String year = dateTime.year.toString();
    String month = dateTime.month.toString();
    String day = dateTime.day.toString();
    String hour = dateTime.hour.toString();
    String minutes = dateTime.minute.toString();
    return "$year-$month-$day $hour:$minutes";
  }
}