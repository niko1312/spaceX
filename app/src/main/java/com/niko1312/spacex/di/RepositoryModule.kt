package com.niko1312.spacex.di

import com.niko1312.spacex.data.repository.LaunchRepositoryImpl
import com.niko1312.spacex.domain.repository.LaunchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLaunchRepository(impl: LaunchRepositoryImpl): LaunchRepository
}
