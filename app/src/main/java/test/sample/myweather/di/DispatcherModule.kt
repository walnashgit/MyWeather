package test.sample.myweather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import test.sample.myweather.DefaultDispatchers
import test.sample.myweather.DispatcherProvider

@Module
@InstallIn(ViewModelComponent::class)
abstract class DispatcherModule {

    @Binds
    abstract fun bindDispatcher(defaultDispatchers: DefaultDispatchers): DispatcherProvider

}