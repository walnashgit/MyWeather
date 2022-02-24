package test.sample.myweather.base

import test.sample.myweather.ui.main.AppEvents

/**
 * Contract for handling location request.
 */
interface BaseLocationUtil {

    /**
     * Fetches current location. Returns the result of fetch in [locationResult].
     */
    fun getCurrentLocation(locationResult: (event: AppEvents) -> Unit)
}