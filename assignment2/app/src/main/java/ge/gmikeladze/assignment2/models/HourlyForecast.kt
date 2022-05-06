package ge.gmikeladze.assignment2.models

data class HourlyForecast(
    var dateTime: String,
    val weatherImage: String,
    val temperature: String,
    val weather: String
)