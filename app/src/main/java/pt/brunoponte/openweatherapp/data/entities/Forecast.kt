package pt.brunoponte.openweatherapp.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *  Format:
 *  {
 *      "dt": Int,
 *      "main": MainInfo,
 *      "weather": List<WeatherInfo>
 *  }
 */
data class Forecast(

    @Expose
    @SerializedName("dt")
    var timestampSecs: Long = -1,

    @Expose
    @SerializedName("main")
    var mainInfo: MainInfo,

    @Expose
    @SerializedName("weather")
    var weatherInfos: List<WeatherInfo> = listOf(),

) {

    /**
     *  Format:
     *  {
     *      "temp": 11.5
     *  }
     */
    data class MainInfo(

        @Expose
        @SerializedName("temp")
        var temperature: Double = Double.NaN,

    )

    /**
     *  Format:
     *  {
     *      "main": "clouds",
     *      "icon": "04n"
     *  }
     */
    data class WeatherInfo(

        @Expose
        @SerializedName("main")
        var type: String = "",

        @Expose
        @SerializedName("icon")
        var icon: String = ""

    )

}
