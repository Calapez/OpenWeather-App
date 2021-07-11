package pt.brunoponte.openweatherapp.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.brunoponte.openweatherapp.data.entities.Hour
import pt.brunoponte.openweatherapp.databinding.HourListItemBinding
import pt.brunoponte.openweatherapp.utils.Helpers
import pt.brunoponte.openweatherapp.utils.Helpers.Companion.roundDecimalPlaces

/**
 *  Adapter for list hours
 */

class HoursAdapter(
    private val hoursList: MutableList<Hour> = mutableListOf()
) : RecyclerView.Adapter<HoursAdapter.HourViewHolder>() {

    /**
     * ViewHolder for the hour view.
     * Contains an image, a temperature, and an hour value
     */
    class HourViewHolder(
        private val binding: HourListItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(hour: Hour) {
            Helpers.fillImageFromUrl(
                binding.imageWeatherType,
                Helpers.calcIconUrl(hour.forecast.weatherInfos[0].icon)
            )
            binding.textHour.text = hour.hourStr.toString()
            binding.textTemperature.text = hour.forecast.mainInfo.temperature
                    .roundDecimalPlaces(1).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val itemBinding = HourListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.bind(hoursList[position])
    }

    override fun getItemCount()
        = hoursList.size

    fun setHours(hours: List<Hour>) {
        hoursList.apply {
            clear()
            addAll(hours)
        }

        notifyDataSetChanged()
    }

}