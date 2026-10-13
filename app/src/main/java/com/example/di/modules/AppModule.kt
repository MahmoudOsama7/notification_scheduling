package com.example.di.modules

import android.content.Context
import com.example.network.XmlParser
import com.example.notification.WorkNotificationScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideXmlParser(
        @ApplicationContext context: Context
    ): XmlParser {
        return XmlParser(context)
    }

    @Provides
    @Singleton
    fun provideWorkNotificationScheduler(
        @ApplicationContext context: Context
    ): WorkNotificationScheduler {
        return WorkNotificationScheduler(context)
    }
}