package com.sabo.core.data.di

import com.sabo.core.data.provider.AuthTokenProvider
import com.sabo.core.data.repository.DiaryRepositoryImpl
import com.sabo.core.data.repository.LoginRepositoryImpl
import com.sabo.core.data.repository.SignUpRepositoryImpl
import com.sabo.core.domain.provider.TokenProvider
import com.sabo.core.domain.repository.DiaryRepository
import com.sabo.core.domain.repository.LoginRepository
import com.sabo.core.domain.repository.SignUpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindSignUpRepository(impl: SignUpRepositoryImpl): SignUpRepository

    @Binds
    abstract fun bindTokenProvider(impl: AuthTokenProvider): TokenProvider

    @Binds
    abstract fun bindDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository
}