package com.sabo.core.navigator.di

import com.sabo.core.navigator.AuthNavigationEventHandler
import com.sabo.core.navigator.NavigationEventHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AuthNavigation

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @AuthNavigation
    @Binds
    @Singleton
    abstract fun bindAuthNavigationEventHandler(
        impl : AuthNavigationEventHandler
    ) : NavigationEventHandler
}