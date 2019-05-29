package idris.adetunmbi.features.moviedetail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val adult: Boolean? = false,
    val budget: Int? = 0,
    @SerialName("genres")
    val genreList: List<Genre?>? = listOf(),
    val id: Int? = 0,
    val overview: String? = "",
    @SerialName("poster_path")
    val posterPath: String? = "",
    @SerialName("release_date")
    val releaseDate: String? = "",
    val runtime: Int? = 0,
    val title: String? = "",
    @SerialName("vote_average")
    val voteAverage: Double? = 0.0,
    @SerialName("vote_count")
    val voteCount: Int? = 0
) {
    val genres: String
        get() {
            val genreNames = genreList?.map { genre -> genre?.name }
            val genreNamesComb = if (genreNames?.size ?: 0 > 3) {
                genreNames?.subList(0, 3)
            } else {
                genreNames
            }
            return genreNamesComb?.joinToString(separator = " | ", postfix = " - ")
                ?: ""
        }

    val releaseYear
        get() = releaseDate?.subSequence(0, 4)

    val rating
        get() = "${voteAverage?.toString()}/10"

    val runt: String
        get() {
            val hrs = runtime?.div(60) ?: 0
            val mins = runtime?.minus(hrs * 60) ?: 0
            return if (hrs == 0 && mins == 0) {
                ""
            } else {
                String.format("%dh %dmin", hrs, mins)
            }

        }
}

@Serializable
data class Genre(
    val id: Int? = 0,
    val name: String? = ""
)