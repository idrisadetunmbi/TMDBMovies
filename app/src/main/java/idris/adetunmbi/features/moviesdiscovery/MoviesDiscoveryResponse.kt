package idris.adetunmbi.features.moviesdiscovery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListResponse(
    val page: Int,
    val results: List<Movie>
)

@Serializable
data class Movie(
    val id: Int,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    val title: String,
    val video: Boolean
)