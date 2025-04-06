package com.app.booktracker.di

import android.content.Context
import androidx.room.Room
import com.app.booktracker.db.ReadingTrackerDatabase
import com.app.booktracker.network.GoogleBooksService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .build()
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideBackendService(retrofit: Retrofit): GoogleBooksService =
        retrofit.create(GoogleBooksService::class.java)

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ReadingTrackerDatabase::class.java,
        "photoplay_db"
    ).build()

    @Singleton
    @Provides
    fun provideBooksDao(db: ReadingTrackerDatabase) = db.bookDao()

    @Singleton
    @Provides
    fun provideProgressDao(db: ReadingTrackerDatabase) = db.progressDao()
}