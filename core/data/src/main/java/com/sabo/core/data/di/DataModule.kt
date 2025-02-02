package com.sabo.core.data.di

import com.sabo.core.data.repository.LoginRepositoryImpl
import com.sabo.core.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository
}