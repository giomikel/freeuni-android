package ge.gmikeladze.assignment2.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private const val BASE_URL = "https://api.openweathermap.org"

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val forecastService: ForecastApi by lazy {
        retrofit().create(ForecastApi::class.java)
    }
}