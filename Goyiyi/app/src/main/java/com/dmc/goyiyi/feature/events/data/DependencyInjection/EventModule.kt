package com.dmc.goyiyi.feature.events.DependencyInjection

import com.dmc.goyiyi.core.network.ApiService
import com.dmc.goyiyi.core.network.RetrofitClient
import com.dmc.goyiyi.feature.events.data.remote.EventRemoteDataSource
import com.dmc.goyiyi.feature.events.data.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {
    @Provides
    @Singleton
    fun provideEventRemoteDataSource(api: ApiService): EventRemoteDataSource =
        EventRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideEventRepository(remote: EventRemoteDataSource): EventRepository =
        EventRepository(remote)
}
