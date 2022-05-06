package ge.gmikeladze.assignment2.models

import com.google.gson.annotations.SerializedName

data class HourlyWeatherModel(@SerializedName("list") val list: List<HourlyListModel>)

data class HourlyListModel(
    @SerializedName("dt") val date: Long,
    @SerializedName("main") val main: HourlyMainFieldModel,
    @SerializedName("weather") val weather: List<HourlyWeatherFieldModel>
)

data class HourlyMainFieldModel(@SerializedName("temp") val temperature: Float)

data class HourlyWeatherFieldModel(
    @SerializedName("icon") val icon: String,
    @SerializedName("description") val desc: String
)