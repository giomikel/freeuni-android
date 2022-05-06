package ge.gmikeladze.assignment2.server

import ge.gmikeladze.assignment2.models.CurrentWeatherModel
import ge.gmikeladze.assignment2.models.HourlyWeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("/data/2.5/weather?appid=$API_KEY")
    fun getCurrent(@Query("q") cityName: String): Call<CurrentWeatherModel>

    @GET("/data/2.5/forecast?appid=$API_KEY")
    fun getHourly(@Query("q") cityName: String): Call<HourlyWeatherModel>

    companion object {
        const val API_KEY = "c909eb384547be1c2c567735c72488c4"
    }
}