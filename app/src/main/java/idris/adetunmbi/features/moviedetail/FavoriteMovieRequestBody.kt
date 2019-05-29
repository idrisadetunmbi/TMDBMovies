package idris.adetunmbi.features.moviedetail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteMovieRequestBody(
    val favorite: Boolean,
    @SerialName("media_id")
    val mediaId: Int,
    @SerialName("media_type")
    val mediaType: String = "movie"
)