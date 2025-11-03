package com.sabo.core.data.di

import com.sabo.core.data.handler.RefreshTokenExpirationHandler
import com.sabo.core.data.provider.AuthTokenProvider
import com.sabo.core.data.repository.DefaultDiaryRepository
import com.sabo.core.data.repository.DefaultLoginRepository
import com.sabo.core.data.repository.DefaultProfileRepository
import com.sabo.core.data.repository.DefaultSignUpRepository
import com.sabo.core.data.repository.DefaultTownRepository
import com.sabo.core.data.repository.DiaryRepository
import com.sabo.core.data.repository.LoginRepository
import com.sabo.core.data.repository.ProfileRepository
import com.sabo.core.data.repository.SignUpRepository
import com.sabo.core.data.repository.TownRepository
import com.sabo.core.model.TokenProvider
import com.sabo.core.network.handler.TokenExpirationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindLoginRepository(impl: DefaultLoginRepository): LoginRepository

    @Binds
    abstract fun bindSignUpRepository(impl: DefaultSignUpRepository): SignUpRepository

    @Binds
    abstract fun bindTokenProvider(impl: AuthTokenProvider): TokenProvider

    @Binds
    abstract fun bindDiaryRepository(impl: DefaultDiaryRepository): DiaryRepository

    @Binds
    abstract fun bindProfileRepository(impl: DefaultProfileRepository): ProfileRepository

    @Binds
    abstract fun bindTownRepository(impl: DefaultTownRepository): TownRepository

    @Binds
    abstract fun bindRefreshTokenExpirationHandler(impl: RefreshTokenExpirationHandler): TokenExpirationHandler
}