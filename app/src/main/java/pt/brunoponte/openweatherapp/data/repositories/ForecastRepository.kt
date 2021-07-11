package pt.brunoponte.openweatherapp.data.repositories

import androidx.lifecycle.MutableLiveData
import pt.brunoponte.openweatherapp.data.entities.Forecast
import pt.brunoponte.openweatherapp.network.Api
import pt.brunoponte.openweatherapp.network.responses.ForecastsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ForecastRepository
@Inject constructor(
    private val api: Api
){

    private val _forecasts = MutableLiveData<List<Forecast>>(listOf())  // All forecasts available
    private val _currentForecast = MutableLiveData<Forecast>() // Forecast of current instant
    private val _isFetching = MutableLiveData<Boolean>(false)

    fun getForecasts()
        = _forecasts

    fun getCurrentForecast()
            = _currentForecast

    fun getIsFetching()
        = _isFetching

    // Fetch forecast of current instant
    fun fetchCurrentForecast(location: String, apiKey: String) {
        _isFetching.value = true

        api.getCurrentForecast(
                location,
                apiKey
        ).enqueue(object: Callback<Forecast> {
            override fun onResponse(
                    call: Call<Forecast>,
                    response: Response<Forecast>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { forecast ->
                        saveCurrentForecast(forecast)
                    }
                }

                _isFetching.postValue(false)
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                t.printStackTrace()
                _isFetching.postValue(false)
            }

        })
    }

    // Fetch forecasts of next 5 days
    fun fetch5DaysForecasts(location: String, apiKey: String) {
        _isFetching.value = true

        api.get5DayForecasts(
            location,
            apiKey
        ).enqueue(object: Callback<ForecastsResponse> {
            override fun onResponse(
                    call: Call<ForecastsResponse>,
                    response: Response<ForecastsResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.forecasts?.let { forecasts ->
                        saveForecasts(forecasts)
                    }
                }

                _isFetching.postValue(false)
            }

            override fun onFailure(call: Call<ForecastsResponse>, t: Throwable) {
                t.printStackTrace()
                _isFetching.postValue(false)
            }

        })
    }

    private fun saveCurrentForecast(forecast: Forecast) {
        _currentForecast.value = forecast
    }

    private fun saveForecasts(forecasts: List<Forecast>) {
        _forecasts.value?.let {
            _forecasts.value = _forecasts.value!!.toMutableList().also {
                it.addAll(forecasts)
            }
        }
    }

}