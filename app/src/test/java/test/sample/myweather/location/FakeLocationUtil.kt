package test.sample.myweather.location

import test.sample.myweather.base.BaseLocationUtil
import test.sample.myweather.ui.main.AppEvents

class FakeLocationUtil: BaseLocationUtil {

    override fun getCurrentLocation(locationResult: (event: AppEvents) -> Unit) {
        //TODO("Not yet implemented")
    }
}