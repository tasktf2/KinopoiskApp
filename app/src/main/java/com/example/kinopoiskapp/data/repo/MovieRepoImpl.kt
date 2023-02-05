package com.example.kinopoiskapp.data.repo

import com.example.kinopoiskapp.data.local.model.MovieEntity
import com.example.kinopoiskapp.data.local.storage.MovieStorage
import com.example.kinopoiskapp.data.remote.api.MovieApi
import com.example.kinopoiskapp.data.remote.response.GetMovieInfoByIdResponse
import com.example.kinopoiskapp.data.remote.response.MovieRemote
import com.example.kinopoiskapp.domain.model.MovieDomain
import com.example.kinopoiskapp.domain.repo.MovieRepo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class MovieRepoImpl(private val api: MovieApi, private val movieStorage: MovieStorage) : MovieRepo {

    override fun getRemotePopularMovies(): Single<List<MovieDomain>> =
        api.getPopularMovies().map { it.movies }.map { it.map(::remoteToDomain) }

    override fun getMovieInfoById(id: Int): Observable<GetMovieInfoByIdResponse> =
        api.getMovieInfoById(id)

    override fun getFavoriteMovies(): Single<List<MovieDomain>> =
        movieStorage.getMovies().map { it.map(::localToDomain) }

    override fun insertFavoriteMovie(movie: MovieDomain):Single<Unit> {
        return movieStorage.insertFavoriteMovie(domainToEntity(movie))
    }

    override fun deleteFavoriteMovie(movie: MovieDomain): Single<Unit> {
        return movieStorage.deleteFavoriteMovie(domainToEntity(movie))
    }

    private fun domainToEntity(domain: MovieDomain): MovieEntity = MovieEntity(
        movieId = domain.movieId,
        movieName = domain.movieName,
        movieYear = domain.movieYear,
        moviePreviewUrl = domain.moviePreviewUrl,
        movieGenres = domain.movieGenres,
        isFavorite = domain.isFavorite
    )

    private fun localToDomain(entity: MovieEntity): MovieDomain = MovieDomain(
        movieId = entity.movieId,
        movieName = entity.movieName,
        movieYear = entity.movieYear,
        moviePreviewUrl = entity.moviePreviewUrl,
        movieGenres = entity.movieGenres,
        isFavorite = entity.isFavorite
    )

    private fun remoteToDomain(remote: MovieRemote): MovieDomain = MovieDomain(
        movieId = remote.movieId,
        movieName = remote.movieName,
        movieYear = remote.movieYear,
        moviePreviewUrl = remote.moviePosterUrlPreview,
        movieGenres = remote.movieGenres.map { it.movieGenre }.first(),
        isFavorite = false
    )

}