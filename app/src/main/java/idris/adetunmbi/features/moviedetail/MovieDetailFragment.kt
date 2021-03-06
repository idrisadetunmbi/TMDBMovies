package idris.adetunmbi.features.moviedetail


import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import idris.adetunmbi.R
import idris.adetunmbi.domain.BASE_IMAGE_URL
import idris.adetunmbi.domain.api.Resource
import idris.adetunmbi.domain.core.BaseFragment
import idris.adetunmbi.domain.extenstions.plusAssign
import idris.adetunmbi.features.moviedetail.MovieDetailViewModel.ViewCommands.ShowNotification
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movie.*
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailFragment : BaseFragment() {

    private val viewModel: MovieDetailViewModel by viewModel()
    private var movieId: Int = 0
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceIsFavList = arguments?.getBoolean(ARG_SOURCE_IS_FAV_LIST)
        sourceIsFavList?.let { setHasOptionsMenu(!it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        progressBar = view.findViewById(R.id.pb_loading)

        subscribeData()
        subscribeCommands()

        movieId = arguments?.getInt(ARG_MOVIE_ID) ?: 0
        viewModel.init(movieId)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_movie_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_favorite -> {
            viewModel.handleFavoriteButtonClick(movieId)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun subscribeData() {
        compositeDisposable += viewModel.dataSubject.subscribe {
            when (it) {
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { movie -> mapMovieToView(movie) }
                }
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    snackBar?.setText(it.message.toString())?.show()
                }
            }
        }
    }

    private fun subscribeCommands() {
        compositeDisposable += viewModel.commandSubject.subscribe {
            when (it) {
                is ShowNotification -> {
                    progressBar.visibility = View.GONE
                    snackBar?.setText(it.message)?.show()
                }
            }
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
        private const val ARG_SOURCE_IS_FAV_LIST = "ARG_SOURCE_IS_FAV_LIST"
        fun args(argMovieId: Int, argSourceIsFavList: Boolean = false): Bundle {
            return bundleOf(ARG_MOVIE_ID to argMovieId, ARG_SOURCE_IS_FAV_LIST to argSourceIsFavList)
        }
    }
}

