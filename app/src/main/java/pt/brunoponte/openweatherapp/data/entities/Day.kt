package pt.brunoponte.openweatherapp.data.entities

data class Day(

    val dayStr: String,
    val forecasts: List<Forecast>

)
