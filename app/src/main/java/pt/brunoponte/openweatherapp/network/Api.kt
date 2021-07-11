package pt.brunoponte.openweatherapp.network

import pt.brunoponte.openweatherapp.data.entities.Forecast
import pt.brunoponte.openweatherapp.network.responses.ForecastsResponse
import pt.brunoponte.openweatherapp.utils.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("forecast?mode=json&units=metric")
    fun get5DayForecasts(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ) : Call<ForecastsResponse>

    @GET("weather?mode=json&units=metric")
    fun getCurrentForecast(
            @Query("q") location: String,
            @Query("appid") apiKey: String
    ) : Call<Forecast>

    companion object {

        operator fun invoke() : Api {
            return Retrofit.Builder()
                .baseUrl(Constants.apiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }

    }

}