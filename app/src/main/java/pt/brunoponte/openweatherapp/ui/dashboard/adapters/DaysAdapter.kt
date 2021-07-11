package pt.brunoponte.openweatherapp.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.brunoponte.openweatherapp.data.entities.Day
import pt.brunoponte.openweatherapp.databinding.DayListItemBinding
import pt.brunoponte.openweatherapp.utils.Helpers.Companion.getAsHours


/**
 *  Adapter for list days
 */

class DaysAdapter(
    private val forecastList: MutableList<Day> = mutableListOf()
) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    /**
     * ViewHolder for the day view.
     * Contains a list of hours through a HoursAdapter
     */
    class DayViewHolder(
        private val binding: DayListItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        private var hoursAdapter: HoursAdapter = HoursAdapter()

        init {
            initHoursRecyclerView()
        }

        fun bind(day: Day) {
            binding.textDate.text = day.dayStr
            hoursAdapter.setHours(day.getAsHours())
        }

        private fun initHoursRecyclerView() {
            binding.recyclerHours.apply {
                this.layoutManager = LinearLayoutManager(context).also {
                    it.orientation = LinearLayoutManager.HORIZONTAL
                }
                this.adapter = hoursAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemBinding = DayListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DayViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount()
        = forecastList.size

    fun setDays(dailyForecasts: List<Day>) {
        forecastList.apply {
            clear()
            addAll(dailyForecasts)
        }

        notifyDataSetChanged()
    }

}