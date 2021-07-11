package pt.brunoponte.openweatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pt.brunoponte.openweatherapp.data.entities.Day
import pt.brunoponte.openweatherapp.data.entities.Forecast
import pt.brunoponte.openweatherapp.data.entities.Hour
import pt.brunoponte.openweatherapp.data.repositories.ForecastRepository
import pt.brunoponte.openweatherapp.utils.Constants
import pt.brunoponte.openweatherapp.utils.Helpers.Companion.getAsDays
import pt.brunoponte.openweatherapp.utils.Helpers.Companion.getAsHours
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel
@Inject constructor(
    private val repository: ForecastRepository
): ViewModel() {

    // Immediately fetches forecast for now and the next 5 days
    init {
        fetchCurrentForecast(
                Constants.defaultLocation,
                Constants.apiKey
        )

        fetch5DaysForecasts(
            Constants.defaultLocation,
            Constants.apiKey
        )
    }

    fun getIsFetching(): LiveData<Boolean> {
        return repository.getIsFetching()
    }

    fun getCurrentForecast(): LiveData<Forecast> {
        return repository.getCurrentForecast()
    }

    fun getAllForecasts(): LiveData<List<Forecast>> {
        return repository.getForecasts()
    }

    // Returns the list of the following 5 days
    fun getNextFiveDays(): List<Day> {
        getAllDays().let {
            if (it.isEmpty()) {
                return listOf()
            }

            return it.drop(1)
        }
    }

    // Returns the list of Hours for today
    fun getTodayHours(): List<Hour> {
        getAllDays().let {
            if (it.isEmpty()) {
                return listOf()
            }

            return it.first().getAsHours()
        }
    }

    // Returns all the days
    private fun getAllDays(): List<Day> {
        return getAllForecasts().value?.getAsDays() ?: listOf()
    }

    // Orders repository to fetch current forecast
    private fun fetchCurrentForecast(location: String, apiKey: String) {
        repository.fetchCurrentForecast(
                location,
                apiKey
        )
    }

    // Orders repository to fetch forecasts for the next 5 days
    private fun fetch5DaysForecasts(location: String, apiKey: String) {
        repository.fetch5DaysForecasts(
            location,
            apiKey
        )
    }

}