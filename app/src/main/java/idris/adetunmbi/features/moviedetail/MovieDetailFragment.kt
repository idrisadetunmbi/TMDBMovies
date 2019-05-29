package idris.adetunmbi.features.moviedetail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import idris.adetunmbi.R
import idris.adetunmbi.domain.BASE_IMAGE_URL
import idris.adetunmbi.domain.extenstions.plusAssign
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movie.*
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailFragment : Fragment() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val viewModel: MovieDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        subscribeData()

        val movieId = arguments?.getInt(ARG_MOVIE_ID)
        movieId?.let { viewModel.init(it) }

        return view
    }

    private fun subscribeData() {
        compositeDisposable += viewModel.dataSubject.subscribe {
            mapMovieToView(it)
        }
    }

    private fun mapMovieToView(movie: MovieResponse) = with(movie) {
        Picasso.get()
            .load("$BASE_IMAGE_URL/$posterPath")
            .fit()
            .centerCrop()
            .into(iv_movie_poster)
        tv_movie_title.text = title
        tv_overview.text = overview
        tv_genres.text = genres
        tv_year.text = releaseYear
        tv_rating.text = rating
        tv_runtime.text = runt
    }

    companion object {
        private const val ARG_MOVIE_ID = "ARG_MOVIE_ID"
        fun args(argMovieId: Int): Bundle {
            return bundleOf(ARG_MOVIE_ID to argMovieId)
        }
    }
}

