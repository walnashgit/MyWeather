package test.sample.myweather.util

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["setTextWithUnit", "unit"], requireAll = true)
fun setTextWithUnit(view: TextView, value: String, unit: String) {
    view.text = "$value$unit"
}