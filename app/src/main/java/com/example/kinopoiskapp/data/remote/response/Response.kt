package com.example.kinopoiskapp.data.remote.response

import com.google.gson.annotations.SerializedName


data class GetPopularMoviesResponse(
    @SerializedName("films") val movies: List<MovieRemote>
)

data class GetMovieInfoByIdResponse(
    @SerializedName("nameRu") val movieName: String,
    @SerializedName("year") val movieYear: String,
    @SerializedName("posterUrl") val moviePosterUrl: String,
    @SerializedName("countries") val movieCountries: List<MovieCountry>,
    @SerializedName("genres") val movieGenres: List<MovieGenre>,
    @SerializedName("description") val movieDescription: String
)

data class MovieInfoById(
    @SerializedName("nameRu") val movieName: String,
    @SerializedName("year") val movieYear: String,
    @SerializedName("posterUrl") val moviePosterUrl: String,
    @SerializedName("countries") val movieCountries: List<String>,
    @SerializedName("genres") val movieGenres: List<String>,
    @SerializedName("description") val movieDescription: String
)

data class MovieRemote(
    @SerializedName("filmId") val movieId: Int,
    @SerializedName("nameRu") val movieName: String,
    @SerializedName("year") val movieYear: String,
    @SerializedName("countries") val movieCountries: List<MovieCountry>,
    @SerializedName("genres") val movieGenres: List<MovieGenre>,
    @SerializedName("rating") val movieRating: String,
    @SerializedName("ratingVoteCount") val movieRatingVoteCount: Int,
    @SerializedName("posterUrl") val moviePosterUrl: String,
    @SerializedName("posterUrlPreview") val moviePosterUrlPreview: String
)

data class MovieGenre(
    @SerializedName("genre") val movieGenre: String
)

data class MovieCountry(
    @SerializedName("country") val movieCountry: String
)