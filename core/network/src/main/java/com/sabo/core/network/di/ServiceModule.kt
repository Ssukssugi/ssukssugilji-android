package com.sabo.core.network.di

import com.sabo.core.network.service.DiaryService
import com.sabo.core.network.service.LoginService
import com.sabo.core.network.service.ProfileService
import com.sabo.core.network.service.SignUpService
import com.sabo.core.network.service.TownService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Singleton
    @Provides
    fun provideSignUpService(retrofit: Retrofit): SignUpService {
        return retrofit.create(SignUpService::class.java)
    }

    @Singleton
    @Provides
    fun provideDiaryService(retrofit: Retrofit): DiaryService {
        return retrofit.create<DiaryService>()
    }

    @Singleton
    @Provides
    fun provideProfileService(retrofit: Retrofit): ProfileService {
        return retrofit.create<ProfileService>()
    }

    @Singleton
    @Provides
    fun provideTownService(retrofit: Retrofit): TownService {
        return retrofit.create<TownService>()
    }
}