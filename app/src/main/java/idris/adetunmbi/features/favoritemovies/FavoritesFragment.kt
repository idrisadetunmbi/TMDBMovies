package idris.adetunmbi.features.favoritemovies


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import idris.adetunmbi.R
import idris.adetunmbi.domain.core.BaseFragment
import idris.adetunmbi.domain.extenstions.plusAssign
import idris.adetunmbi.features.moviedetail.MovieDetailFragment
import idris.adetunmbi.features.moviesdiscovery.MoviesListAdapter
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel

class FavoritesFragment : BaseFragment() {

    private val viewModel: FavoriteMoviesViewModel by viewModel()
    private val moviesListAdapter: MoviesListAdapter =
        MoviesListAdapter(
            mutableListOf()
        ) { movieId ->
            findNavController()
                .navigate(R.id.action_global_movieFragment, MovieDetailFragment.args(movieId, true))
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        initializeRvListContainer(view)
        subscribeData()
        return view
    }

    private fun initializeRvListContainer(view: View?) {
        view?.findViewById<RecyclerView>(R.id.rv_list_container)?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = moviesListAdapter
        }
    }

    private fun subscribeData() {
        compositeDisposable += viewModel.dataSubject.subscribe {
            moviesListAdapter.updateData(it.results)
        }
    }
}
