package test.sample.myweather.ui.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import test.sample.myweather.R
import test.sample.myweather.data.onecall.Hourly
import test.sample.myweather.databinding.HourlyForecastItemBinding
import test.sample.myweather.ui.BaseRecyclerViewAdapter

class HourlyDataAdapter: BaseRecyclerViewAdapter<Hourly, HourlyDataAdapter.HourlyDataViewHolder>(
    diffCallback = HourlyDataDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyDataViewHolder {
        return HourlyDataViewHolder(createBinding(parent))
    }

    override fun onBindViewHolder(holder: HourlyDataViewHolder, position: Int) {
        (holder.binding as HourlyForecastItemBinding).hourly = getItem(position)
        holder.binding.executePendingBindings()
    }

    private fun createBinding(parent: ViewGroup): HourlyForecastItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.hourly_forecast_item,
            parent,
            false
        )
    }

    inner class HourlyDataViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)
}