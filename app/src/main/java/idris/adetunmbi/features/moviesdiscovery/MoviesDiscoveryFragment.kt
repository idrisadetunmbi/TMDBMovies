package idris.adetunmbi.features.moviesdiscovery


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import idris.adetunmbi.features.moviedetail.MovieDetailFragment
import idris.adetunmbi.R
import idris.adetunmbi.domain.api.Resource
import idris.adetunmbi.domain.extenstions.plusAssign
import idris.adetunmbi.features.moviesdiscovery.MoviesDiscoveryViewModel.ViewCommands.ClearData
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesDiscoveryFragment : Fragment() {
    private val viewModel: MoviesDiscoveryViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    private val moviesListAdapter: MoviesListAdapter by lazy {
        MoviesListAdapter(
            mutableListOf()
        ) { movieId ->
            findNavController()
                .navigate(R.id.action_moviesDiscoveryFragment_to_movieFragment, MovieDetailFragment.args(movieId))
        }
    }
    private val snackBar: Snackbar? by lazy {
        this.view?.let {
            Snackbar.make(it, "", Snackbar.LENGTH_SHORT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_discovery, container, false)
        initializeRvListContainer(view)
        subscribeData()
        subscribeCommands()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handleSearchSubmit(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { handleSearchChange(it) }
                return true
            }
        })
    }

    fun handleSearchSubmit(text: String?) {
        if (text == null || text.isBlank()) return
        moviesListAdapter.clearData()
        viewModel.handleSearchSubmit(text)
    }

    fun handleSearchChange(newText: String) {
        viewModel.handleSearchChange(newText)
    }

    private fun initializeRvListContainer(view: View?) {
        view?.findViewById<RecyclerView>(R.id.rv_list_container)?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = moviesListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val hasReachedListEnd = !canScrollVertically(1)
                    if (hasReachedListEnd) viewModel.loadNextPage()
                }
            })
        }
    }

    private fun subscribeData() {
        compositeDisposable.add(
            viewModel.dataSubject.subscribe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // TODO: handle loading state
                    }
                    is Resource.Success -> {
                        resource.data?.let {
                            moviesListAdapter.updateData(it)
                        }
                    }
                    is Resource.Error -> {
                        snackBar?.setText("An error occurred")?.show()
                    }
                }
            }
        )
    }

    private fun subscribeCommands() {
        compositeDisposable += viewModel.commandSubject.subscribe {
            when (it) {
                is ClearData -> moviesListAdapter.clearData()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
