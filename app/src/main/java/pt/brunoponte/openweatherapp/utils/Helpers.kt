package pt.brunoponte.openweatherapp.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso
import pt.brunoponte.openweatherapp.data.entities.Day
import pt.brunoponte.openweatherapp.data.entities.Forecast
import pt.brunoponte.openweatherapp.data.entities.Hour
import java.text.SimpleDateFormat
import java.util.*

class Helpers {

    companion object {

        // Uses picasso to fill an imageView from a Url
        fun fillImageFromUrl(imgView: ImageView, url: String) {
            Picasso.get()
                .load(url)
                .into(imgView)
        }


        // Calculates the weather icon url based on its name
        fun calcIconUrl(weatherType: String) =
            Constants.iconsUrl.format(weatherType)


        // Rounds a decimal number's decimal places
        fun Double.roundDecimalPlaces(decimalPlaces: Int) : Double =
                String.format(
                        Locale.US,
                        "%.${decimalPlaces}f",
                        this
                ).toDouble()


        // Creates a list of Days from a list of Forecasts
        fun List<Forecast>.getAsDays() : List<Day> {
            val forecastsByDay = this.groupBy { forecast ->
                SimpleDateFormat("dd MMMM yyyy", Locale.US)
                    .format(
                        timestampSecsToDate(forecast.timestampSecs)
                    )
            }

            return mutableListOf<Day>().also { days ->
                forecastsByDay.forEach { (dayStr, forecasts) ->
                    days.add(
                        Day(dayStr, forecasts)
                    )
                }
            }
        }


        // Creates a list of hours from a day
        fun Day.getAsHours() : List<Hour> {
            val forecastsByHour = this.forecasts.groupBy { forecast ->
                SimpleDateFormat("HH", Locale.US)
                    .format(
                        timestampSecsToDate(forecast.timestampSecs)
                    )
            }

            return mutableListOf<Hour>().also { hours ->
                forecastsByHour.forEach { (hourStr, forecasts) ->
                    hours.add(
                        Hour(hourStr, forecasts[0])
                    )
                }
            }
        }


        // Creates a Date object from a timestamp in seconds
        private fun timestampSecsToDate(timestampSecs: Long)
            = Date(timestampSecs * 1000)
    }

}