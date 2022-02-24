package test.sample.myweather.util

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import test.sample.myweather.data.onecall.Hourly
import test.sample.myweather.ui.hourly.HourlyDataAdapter

/**
 * Custom binding adapter for adding 'unit' symbol with any text in a textview.
 */
@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["setTextWithUnit", "unit"], requireAll = true)
fun setTextWithUnit(view: TextView, value: String, unit: String) {
    view.text = "$value$unit"
}

/**
 * Custom binding adapter for submitting a list to a recyclerview.
 */
@BindingAdapter("listData")
fun <T> bindRecyclerView(recyclerView: RecyclerView, data: List<T>?) {
    if (recyclerView.adapter is HourlyDataAdapter && !data.isNullOrEmpty()) {
        (recyclerView.adapter as HourlyDataAdapter).submitList(data as List<Hourly>)
    }
}