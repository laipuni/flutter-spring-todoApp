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

  static DateTime getKoreaNow(){
    //UTC (영국 천문대를 기준)에서 9시간을 더하면 한국 시간
    return DateTime.now().toLocal().add(Duration(hours: 9));
  }
}