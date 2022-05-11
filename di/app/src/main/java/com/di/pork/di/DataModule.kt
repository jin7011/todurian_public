package com.di.pork.di

import android.content.Context
import android.content.SharedPreferences
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideService(): Service{
        return Service.create()
    }

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getPreferences(context)
    }
}