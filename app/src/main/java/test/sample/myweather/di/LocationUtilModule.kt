package test.sample.myweather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import test.sample.myweather.base.BaseLocationPermissionUtil
import test.sample.myweather.base.BaseLocationUtil
import test.sample.myweather.location.LocationPermissionUtil
import test.sample.myweather.location.LocationUtil

@Module
@InstallIn(ActivityComponent::class)
abstract class LocationUtilModule {

    @Binds
    abstract fun bindLocationPermissionUtil(locationPermissionUtil: LocationPermissionUtil): BaseLocationPermissionUtil

    @Binds
    abstract fun bindLocationUtil(locationUtil: LocationUtil): BaseLocationUtil
}