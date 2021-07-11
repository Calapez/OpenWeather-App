package pt.brunoponte.openweatherapp.network.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import pt.brunoponte.openweatherapp.data.entities.Forecast

/**
 *  Format:
 *  {
 *      "list": [Forecast, ...]
 *  }
 */
data class ForecastsResponse(

    @Expose
    @SerializedName("list")
    var forecasts: List<Forecast> = listOf()

)
