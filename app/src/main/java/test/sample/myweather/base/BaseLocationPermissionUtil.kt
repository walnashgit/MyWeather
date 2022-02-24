package test.sample.myweather.base

/**
 * Contract for handling location permission requests.
 */
interface BaseLocationPermissionUtil {
    /**
     * Checks for location permission for this app and returns true if granted false otherwise
     */
    fun checkLocationPermission(): Boolean

    /**
     * Makes a request for location permission from user.
     */
    fun requestLocationPermission()

    /**
     * Checks if GPS is enabled in the device.
     */
    fun isLocationEnabled(): Boolean

    /**
     * Shows a dialog requesting user to enable GPS.
     */
    fun showGPSNotEnabledDialog()

    /**
     * Checks if a rationale should be shown before requesting for permission.
     */
    fun showRationale(): Boolean
}