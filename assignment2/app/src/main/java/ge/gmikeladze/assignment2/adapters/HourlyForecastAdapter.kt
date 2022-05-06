package ge.gmikeladze.assignment2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.gmikeladze.assignment2.models.HourlyForecast
import ge.gmikeladze.assignment2.MainActivity.Companion.WEATHER_IMAGE_URL_LEFT
import ge.gmikeladze.assignment2.MainActivity.Companion.WEATHER_IMAGE_URL_RIGHT
import ge.gmikeladze.assignment2.R

class HourlyForecastAdapter :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    private var items: List<HourlyForecast> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_item, parent, false)
        return HourlyForecastViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HourlyForecastViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<HourlyForecast>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTime: TextView = itemView.findViewById(R.id.hourlyDate)
        private val weatherImage: ImageView = itemView.findViewById(R.id.hourlyWeatherImage)
        private val temperature: TextView = itemView.findViewById(R.id.hourlyTemperature)
        private val weather: TextView = itemView.findViewById(R.id.hourlyWeather)

        fun bind(hourlyForecast: HourlyForecast) {
            dateTime.text = hourlyForecast.dateTime
            Glide.with(weatherImage.context)
                .load(
                    WEATHER_IMAGE_URL_LEFT
                            + hourlyForecast.weatherImage
                            + WEATHER_IMAGE_URL_RIGHT
                )
                .into(weatherImage)
            temperature.text = hourlyForecast.temperature
            weather.text = hourlyForecast.weather
        }
    }
}