package pt.brunoponte.openweatherapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import pt.brunoponte.openweatherapp.data.entities.Day
import pt.brunoponte.openweatherapp.data.entities.Forecast
import pt.brunoponte.openweatherapp.data.entities.Hour
import pt.brunoponte.openweatherapp.databinding.FragmentDashboardBinding
import pt.brunoponte.openweatherapp.ui.dashboard.adapters.DaysAdapter
import pt.brunoponte.openweatherapp.utils.Helpers.Companion.calcIconUrl
import pt.brunoponte.openweatherapp.utils.Helpers.Companion.fillImageFromUrl
import pt.brunoponte.openweatherapp.utils.Helpers.Companion.roundDecimalPlaces
import pt.brunoponte.openweatherapp.viewmodels.ForecastViewModel

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val viewModel: ForecastViewModel by viewModels()
    private lateinit var binding: FragmentDashboardBinding  // ViewBinding
    private lateinit var daysAdapter: DaysAdapter  // Adapter for list of days

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        daysAdapter = DaysAdapter()
        daysAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy
            .PREVENT_WHEN_EMPTY // Keep list state
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDaysRecyclerView()
        setObservers()
    }

    private fun initDaysRecyclerView() {
        binding.recyclerDays.apply {
            this.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }
            this.adapter = daysAdapter
        }
    }

    private fun setObservers() {
        viewModel.getCurrentForecast().observe(viewLifecycleOwner) { forecast ->
            // New current forecast, update its view
            updateCurrentForecast(forecast)
        }

        viewModel.getAllForecasts().observe(viewLifecycleOwner) {
            // update the list and the chart
            updateForecastsList(viewModel.getNextFiveDays())

            // Update the chart
            fillChart(viewModel.getTodayHours())
        }

        viewModel.getIsFetching().observe(viewLifecycleOwner) { isFetching ->
            // Fetching state changed, update progress bar
            binding.progressBar.visibility = if (isFetching) VISIBLE else INVISIBLE
        }
    }

    // Update View content for the list of forecasts
    private fun updateForecastsList(days: List<Day>) {
        daysAdapter.setDays(days)
    }

    // Update View content for current instant's forecast
    private fun updateCurrentForecast(forecast: Forecast) {
        binding.apply {
            textTemperature.text = forecast.mainInfo.temperature
                    .roundDecimalPlaces(1).toString()

            textWeatherType.text = forecast.weatherInfos[0].type

            fillImageFromUrl(
                    imageWeatherType,
                    calcIconUrl(forecast.weatherInfos[0].icon)
            )

            layoutCurrentForecast.visibility = VISIBLE
        }
    }

    // Set View content for the chart
    private fun fillChart(hours: List<Hour>) {
        // Set Entry<hour, temperature> values for chart
        val entries = hours.map { hour ->
            Entry(
                hour.hourStr.toFloat(),
                hour.forecast.mainInfo.temperature.toFloat()
            )
        }

        // Create dataset with respective style
        val dataSet = LineDataSet(entries, "Hours").also {
            it.label = "Temperature"
            it.setDrawFilled(true)
            it.valueTextSize = 10f
        }

        // Add dataset to chart and customize chart
        binding.temperatureChart.let { chart ->
            chart.data = LineData(dataSet)
            chart.axisLeft.isEnabled = false
            chart.axisRight.isEnabled = false
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.invalidate()
            chart.animateXY(1500, 1500)
        }
    }
}