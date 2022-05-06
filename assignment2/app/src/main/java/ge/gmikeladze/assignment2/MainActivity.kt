package ge.gmikeladze.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ge.gmikeladze.assignment2.adapters.ForecastViewPagerAdapter
import ge.gmikeladze.assignment2.fragments.HourlyFragment
import ge.gmikeladze.assignment2.fragments.NowFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var fragments: List<Fragment>
    private lateinit var weatherNowButton: ImageButton
    private lateinit var weatherHourlyButton: ImageButton
    private lateinit var currentCity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentCity = getString(R.string.city_name)
        mainLayout = findViewById(R.id.mainConstraintLayout)

        fragments = listOf(NowFragment(), HourlyFragment())
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ForecastViewPagerAdapter(this, fragments)

        weatherNowButton = findViewById(R.id.weatherNowButton)
        weatherNowButton.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }

        weatherHourlyButton = findViewById(R.id.weatherHourlyButton)
        weatherHourlyButton.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }
    }

    companion object {
        const val GEORGIAN_FLAG_CITY: String = "TBILISI"
        const val UK_FLAG_CITY: String = "LONDON"
        const val JAMAICAN_FLAG_CITY: String = "KINGSTON"
        const val WEATHER_IMAGE_URL_LEFT = "https://openweathermap.org/img/wn/"
        const val WEATHER_IMAGE_URL_RIGHT = "@2x.png"
        const val KELVIN_SCALE_OFFSET = 273.15
        const val DEGREE_SYMBOL = "°"
        lateinit var mainLayout: ConstraintLayout
    }
}