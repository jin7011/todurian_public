package com.di.pork.di

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.di.pork.model.PreferenceManager
import com.di.pork.utility.Utility.Companion.getLinearLayoutManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
class UtilityModule {

    @FragmentScoped
    @Provides
    fun provideUtility(@ActivityContext context: Context): LinearLayoutManager {
        return getLinearLayoutManager(context)
    }
}