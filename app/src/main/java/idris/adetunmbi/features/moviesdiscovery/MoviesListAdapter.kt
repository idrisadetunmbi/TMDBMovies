package idris.adetunmbi.features.moviesdiscovery

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import idris.adetunmbi.R
import idris.adetunmbi.domain.BASE_IMAGE_URL


class MoviesListAdapter(private var movies: MutableList<Movie>) : RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateData(newMovies: List<Movie>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val overviewTextView by lazy {
            itemView.findViewById<TextView>(R.id.textview_movie_overview)
        }
        private val imagePosterImageView: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.imageview_movie_poster)
        }

        fun bind(movie: Movie) {
            overviewTextView.text = movie.overview
            Picasso.get()
                .load("$BASE_IMAGE_URL/${movie.posterPath}")
                .fit()
                .centerCrop()
                .into(imagePosterImageView)
        }
    }
}

fun getTransformation(imageView: ImageView): Transformation = object : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val targetWidth = imageView.width

        val aspectRatio = source.height.toDouble() / source.width.toDouble()
        val targetHeight = (targetWidth * aspectRatio).toInt()
        val result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle()
        }
        return result
    }

    override fun key(): String {
        return "transformation" + " desiredWidth"
    }
}