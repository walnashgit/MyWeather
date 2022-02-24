package test.sample.myweather.ui.hourly

import androidx.recyclerview.widget.DiffUtil
import test.sample.myweather.data.onecall.Hourly

/**
 * Diff callback for checking difference between hourly weather data.
 */
class HourlyDataDiffCallback: DiffUtil.ItemCallback<Hourly>() {

    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return (oldItem.dt == newItem.dt)
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return (oldItem.temp == newItem.temp ||
                oldItem.weather[0].getDescription() == newItem.weather[0].getDescription())
    }
}