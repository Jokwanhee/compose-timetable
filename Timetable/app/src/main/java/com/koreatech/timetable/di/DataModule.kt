package com.koreatech.timetable.di

import android.content.Context
import androidx.compose.ui.tooling.preview.Preview
import com.koreatech.timetable.data.datastore.DataStoreImpl
import com.koreatech.timetable.data.source.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providesLocalDataSource(
        @ApplicationContext context: Context,
    ): LocalDataSource = LocalDataSource(context)

    @Provides
    @Singleton
    fun providesDataStoreImpl(
        @ApplicationContext context: Context
    ): DataStoreImpl = DataStoreImpl(context)
}