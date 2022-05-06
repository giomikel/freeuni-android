package ge.gmikeladze.assignment2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import ge.gmikeladze.assignment2.R
import ge.gmikeladze.assignment2.server.RetrofitService
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import ge.gmikeladze.assignment2.MainActivity.Companion.DEGREE_SYMBOL
import ge.gmikeladze.assignment2.MainActivity.Companion.GEORGIAN_FLAG_CITY
import ge.gmikeladze.assignment2.MainActivity.Companion.JAMAICAN_FLAG_CITY
import ge.gmikeladze.assignment2.MainActivity.Companion.KELVIN_SCALE_OFFSET
import ge.gmikeladze.assignment2.MainActivity.Companion.UK_FLAG_CITY
import ge.gmikeladze.assignment2.MainActivity.Companion.WEATHER_IMAGE_URL_LEFT
import ge.gmikeladze.assignment2.MainActivity.Companion.WEATHER_IMAGE_URL_RIGHT
import ge.gmikeladze.assignment2.MainActivity.Companion.mainLayout
import ge.gmikeladze.assignment2.isDayTime
import ge.gmikeladze.assignment2.models.CurrentWeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class NowFragment : Fragment() {

    private val forecastApi = RetrofitService.forecastService
    private lateinit var georgiaButton: ImageButton
    private lateinit var ukButton: ImageButton
    private lateinit var jamaicaButton: ImageButton
    private lateinit var cityNameTextView: TextView
    private lateinit var weatherImage: ImageView
    private lateinit var temperatureTextView: TextView
    private lateinit var weatherTextView: TextView
    private lateinit var detailsTemperatureNum: TextView
    private lateinit var detailsFeelsLikeNum: TextView
    private lateinit var detailsHumidityNum: TextView
    private lateinit var detailsPressureNum: TextView
    private var currentCity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("hourlyFragment") { _, bundle ->
            currentCity = bundle.getString("currentCity").toString()
            view?.let { updateFragment(it) }
        }
        if (currentCity == null) currentCity = GEORGIAN_FLAG_CITY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFragment(view)
    }

    private fun updateFragment(view: View) {
        getViews(view)
        cityNameTextView.text = currentCity
        setOnClickListeners()
        getCurrentCityData()
    }

    private fun getCurrentCityData() {
        forecastApi.getCurrent(currentCity!!).enqueue(object : Callback<CurrentWeatherModel> {
            override fun onResponse(
                call: Call<CurrentWeatherModel>,
                response: Response<CurrentWeatherModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { setViewValues(it) }
                }
            }

            override fun onFailure(call: Call<CurrentWeatherModel>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setViewValues(currentWeather: CurrentWeatherModel) {
        Glide.with(this)
            .load(WEATHER_IMAGE_URL_LEFT + currentWeather.weather[0].icon + WEATHER_IMAGE_URL_RIGHT)
            .into(weatherImage)
        val temperatureText =
            (currentWeather.main.temperature - KELVIN_SCALE_OFFSET).roundToInt()
                .toString() + DEGREE_SYMBOL
        temperatureTextView.text = temperatureText
        weatherTextView.text = currentWeather.weather[0].desc.uppercase()
        detailsTemperatureNum.text = temperatureText
        val feelsLikeText = (currentWeather.main.feelsLike - KELVIN_SCALE_OFFSET).roundToInt()
            .toString() + DEGREE_SYMBOL
        detailsFeelsLikeNum.text = feelsLikeText
        val humidityText = currentWeather.main.humidity.toString() + "%"
        detailsHumidityNum.text = humidityText
        detailsPressureNum.text = currentWeather.main.pressure.toString()
        if (isDayTime(currentWeather.dateTime, currentCity!!))
            mainLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_color_day
                )
            )
        else
            mainLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_color_night
                )
            )

    }

    private fun setOnClickListeners() {
        georgiaButton.setOnClickListener {
            setFragmentResultOnButtonClicked(GEORGIAN_FLAG_CITY)
            currentCity = GEORGIAN_FLAG_CITY
            updateFragment(requireView())
        }
        ukButton.setOnClickListener {
            setFragmentResultOnButtonClicked(UK_FLAG_CITY)
            currentCity = UK_FLAG_CITY
            updateFragment(requireView())
        }
        jamaicaButton.setOnClickListener {
            setFragmentResultOnButtonClicked(JAMAICAN_FLAG_CITY)
            currentCity = JAMAICAN_FLAG_CITY
            updateFragment(requireView())
        }
    }

    private fun setFragmentResultOnButtonClicked(city: String) {
        setFragmentResult("currentFragment", bundleOf("currentCity" to city))
    }

    private fun getViews(view: View) {
        georgiaButton = view.findViewById(R.id.georgiaButton)
        ukButton = view.findViewById(R.id.UKButton)
        jamaicaButton = view.findViewById(R.id.jamaicaButton)
        cityNameTextView = view.findViewById(R.id.cityNameTextView)
        weatherImage = view.findViewById(R.id.weatherImage)
        temperatureTextView = view.findViewById(R.id.temperatureTextView)
        weatherTextView = view.findViewById(R.id.weatherTextView)
        detailsTemperatureNum = view.findViewById(R.id.detailsTemperatureNum)
        detailsFeelsLikeNum = view.findViewById(R.id.detailsFeelsLikeNum)
        detailsHumidityNum = view.findViewById(R.id.detailsHumidityNum)
        detailsPressureNum = view.findViewById(R.id.detailsPressureNum)
    }
}