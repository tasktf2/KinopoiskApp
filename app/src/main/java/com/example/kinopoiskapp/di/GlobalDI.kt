package com.example.kinopoiskapp.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.kinopoiskapp.MovieApp
import com.example.kinopoiskapp.data.local.db.AppDatabase
import com.example.kinopoiskapp.data.local.db.dao.MovieDao
import com.example.kinopoiskapp.data.local.storage.MovieStorage
import com.example.kinopoiskapp.data.remote.api.MovieApi
import com.example.kinopoiskapp.data.repo.MovieRepoImpl
import com.example.kinopoiskapp.domain.repo.MovieRepo
import com.example.kinopoiskapp.domain.usecase.*
import com.example.kinopoiskapp.presentation.ui.description.DescriptionState
import com.example.kinopoiskapp.presentation.ui.description.DescriptionViewModelFactory
import com.example.kinopoiskapp.presentation.ui.main.MoviesState
import com.example.kinopoiskapp.presentation.ui.main.MoviesViewModelFactory
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
object GlobalDI {
    private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"
    private const val API_KEY = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
    private const val CONNECT_TIMEOUT = 10L
    private const val READ_TIMEOUT = 10L
    private const val HEADER_NAME = "X-API-KEY"
    private const val DATABASE_NAME = "movie.db"

    private val context: Context? by lazy { MovieApp.appContext }


    private val interceptor: Interceptor by lazy {
        Interceptor { chain ->
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                .header(HEADER_NAME, API_KEY)
                .build()
            chain.proceed(authenticatedRequest)
        }
    }

    private val networkInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addNetworkInterceptor(networkInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private val moviesState: MoviesState by lazy { MoviesState() }
    private val descriptionState: DescriptionState by lazy { DescriptionState() }

    private val scheduler: Scheduler by lazy { Schedulers.io() }

    private val movieApi: MovieApi by lazy { retrofit.create(MovieApi::class.java) }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context!!.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }


    private val movieDao: MovieDao by lazy { database.movieDao() }

    private val movieStorage = MovieStorage(movieDao)

    private val movieRepo: MovieRepo by lazy { MovieRepoImpl(movieApi, movieStorage) }

    private val getPopularMoviesUseCase: GetPopularMoviesUseCase by lazy {
        GetPopularMoviesUseCase(movieRepo, scheduler)
    }

    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase by lazy {
        GetFavoriteMoviesUseCase(movieRepo, scheduler)
    }

    private val getMovieInfoByIdUseCase: GetMovieInfoByIdUseCase by lazy {
        GetMovieInfoByIdUseCase(movieRepo, scheduler)
    }

    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase by lazy {
        InsertFavoriteMovieUseCase(
            movieRepo,
            scheduler
        )
    }

    private val deleteFavoriteMovieUseCase by lazy {
        DeleteFavoriteMovieUseCase(
            movieRepo,
            scheduler
        )
    }

    val moviesViewModelFactory: MoviesViewModelFactory by lazy {
        MoviesViewModelFactory(
            moviesState,
            getPopularMoviesUseCase,
            getFavoriteMoviesUseCase,
            insertFavoriteMovieUseCase,
            deleteFavoriteMovieUseCase
        )
    }

    val descriptionViewModelFactory = DescriptionViewModelFactory(
        descriptionState,
        getMovieInfoByIdUseCase
    )
}