package ge.gmikeladze.assignment2.models

import com.google.gson.annotations.SerializedName

data class CurrentWeatherModel(
    @SerializedName("weather") val weather: List<WeatherFieldModel>,
    @SerializedName("main") val main: MainFieldModel,
    @SerializedName("dt") val dateTime: Long
)

data class WeatherFieldModel(
    @SerializedName("icon") val icon: String,
    @SerializedName("description") val desc: String
)

data class MainFieldModel(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("pressure") val pressure: Int
)