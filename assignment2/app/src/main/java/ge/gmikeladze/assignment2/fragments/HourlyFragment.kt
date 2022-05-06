package ge.gmikeladze.assignment2.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.gmikeladze.assignment2.MainActivity
import ge.gmikeladze.assignment2.MainActivity.Companion.DEGREE_SYMBOL
import ge.gmikeladze.assignment2.MainActivity.Companion.KELVIN_SCALE_OFFSET
import ge.gmikeladze.assignment2.R
import ge.gmikeladze.assignment2.adapters.HourlyForecastAdapter
import ge.gmikeladze.assignment2.models.HourlyForecast
import ge.gmikeladze.assignment2.models.HourlyWeatherModel
import ge.gmikeladze.assignment2.server.RetrofitService
import ge.gmikeladze.assignment2.unixToDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class HourlyFragment : Fragment() {

    private var currentCity: String? = null
    private val forecastApi = RetrofitService.forecastService
    private lateinit var georgiaButton: ImageButton
    private lateinit var ukButton: ImageButton
    private lateinit var jamaicaButton: ImageButton
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var hourlyForecastAdapter: HourlyForecastAdapter
    private lateinit var cityNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("currentFragment") { _, bundle ->
            currentCity = bundle.getString("currentCity").toString()
            view?.let { updateFragment(it) }
        }
        if (currentCity == null) currentCity = MainActivity.GEORGIAN_FLAG_CITY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly, container, false)
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
        forecastApi.getHourly(currentCity!!).enqueue(object : Callback<HourlyWeatherModel> {
            override fun onResponse(
                call: Call<HourlyWeatherModel>,
                response: Response<HourlyWeatherModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { setViewValues(it) }
                }
            }

            override fun onFailure(call: Call<HourlyWeatherModel>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setViewValues(hourlyWeatherList: HourlyWeatherModel) {
        val hourlyForecastList: MutableList<HourlyForecast> = ArrayList()
        for (hourlyWeather in hourlyWeatherList.list) {
            val hourlyForecast = HourlyForecast(
                unixToDate(hourlyWeather.date, currentCity!!),
                hourlyWeather.weather[0].icon,
                (hourlyWeather.main.temperature - KELVIN_SCALE_OFFSET).roundToInt()
                    .toString() + DEGREE_SYMBOL,
                hourlyWeather.weather[0].desc
            )
            hourlyForecastList.add(hourlyForecast)
        }
        hourlyForecastAdapter.updateItems(hourlyForecastList)
    }

    private fun getViews(view: View) {
        cityNameTextView = view.findViewById(R.id.cityNameTextView)
        georgiaButton = view.findViewById(R.id.georgiaButton)
        ukButton = view.findViewById(R.id.UKButton)
        jamaicaButton = view.findViewById(R.id.jamaicaButton)
        hourlyRecyclerView = view.findViewById(R.id.hourlyForecastRecyclerView)
        hourlyRecyclerView.layoutManager = LinearLayoutManager(context)
        hourlyForecastAdapter = HourlyForecastAdapter()
        hourlyRecyclerView.adapter = hourlyForecastAdapter
        val divider = DividerItemDecoration(
            hourlyRecyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        divider.setDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.divider_color)))
        hourlyRecyclerView.addItemDecoration(divider)
    }

    private fun setOnClickListeners() {
        georgiaButton.setOnClickListener {
            setFragmentResultOnButtonClicked(MainActivity.GEORGIAN_FLAG_CITY)
            currentCity = MainActivity.GEORGIAN_FLAG_CITY
            updateFragment(requireView())
        }
        ukButton.setOnClickListener {
            setFragmentResultOnButtonClicked(MainActivity.UK_FLAG_CITY)
            currentCity = MainActivity.UK_FLAG_CITY
            updateFragment(requireView())
        }
        jamaicaButton.setOnClickListener {
            setFragmentResultOnButtonClicked(MainActivity.JAMAICAN_FLAG_CITY)
            currentCity = MainActivity.JAMAICAN_FLAG_CITY
            updateFragment(requireView())
        }
    }

    private fun setFragmentResultOnButtonClicked(city: String) {
        setFragmentResult("hourlyFragment", bundleOf("currentCity" to city))
    }
}