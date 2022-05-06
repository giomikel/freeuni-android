package ge.gmikeladze.assignment2

import ge.gmikeladze.assignment2.MainActivity.Companion.JAMAICAN_FLAG_CITY
import ge.gmikeladze.assignment2.MainActivity.Companion.UK_FLAG_CITY
import java.util.*

fun unixToDate(unixTime: Long, city: String): String {
    val sdf = java.text.SimpleDateFormat("hh aa dd MMMM")
    sdf.timeZone = getTimezone(city)!!
    val date = Date(unixTime * 1000)
    return sdf.format(date)
}

fun isDayTime(unixTime: Long, city: String): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = (unixTime * 1000)
    calendar.timeZone = getTimezone(city)!!
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    return hours in 6..17
}

private fun getTimezone(city: String): TimeZone? {
    var timezone = TimeZone.getTimeZone("Asia/Tbilisi")
    if (city == UK_FLAG_CITY) timezone = TimeZone.getTimeZone("Europe/London")
    if (city == JAMAICAN_FLAG_CITY) timezone = TimeZone.getTimeZone("America/Jamaica")
    return timezone
}