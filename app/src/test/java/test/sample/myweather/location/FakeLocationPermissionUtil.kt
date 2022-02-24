package test.sample.myweather.location

import test.sample.myweather.base.BaseLocationPermissionUtil

class FakeLocationPermissionUtil: BaseLocationPermissionUtil {

    var enableLocation = false
    var locationPermissionGranted = false
    var showRationale = false

    override fun checkLocationPermission(): Boolean {
        if (locationPermissionGranted)
            return true
        return false
    }

    override fun requestLocationPermission() {
        //Do Nothing
    }

    override fun isLocationEnabled(): Boolean {
        if (enableLocation)
            return true
        return false
    }

    override fun showGPSNotEnabledDialog() {
        //DoNothing
    }

    override fun showRationale(): Boolean {
        if (showRationale)
            return true
        return false
    }

}