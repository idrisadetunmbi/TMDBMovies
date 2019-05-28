package idris.adetunmbi.features.moviesdiscovery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import idris.adetunmbi.R
import idris.adetunmbi.domain.api.Resource
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesDiscoveryFragment : Fragment() {

    private val viewModel: MoviesDiscoveryViewModel by viewModel()

    private val moviesListAdapter: MoviesListAdapter by lazy {
        MoviesListAdapter(
            mutableListOf()
        )
    }
    private val snackBar: Snackbar? by lazy {
        this.view?.let {
            Snackbar.make(it, "", Snackbar.LENGTH_SHORT)
        }
    }

    private val compositeDisposable = CompositeDisposable()
    private var fetchTriggered: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_discovery, container, false)
        initializeRvListContainer(view)
        subscribeData()
        return view
    }

    private fun initializeRvListContainer(view: View?) {
        view?.findViewById<RecyclerView>(R.id.rv_list_container)?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = moviesListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1) && !fetchTriggered) {
                        fetchTriggered = true
                        viewModel.loadNextPage()
                    }
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
                        fetchTriggered = false
                        resource.data?.let {
                            moviesListAdapter.updateData(it)
                        }
                    }
                    is Resource.Error -> {
                        fetchTriggered = false
                        snackBar?.setText("An error occurred")?.show()
                    }
                }
            }
        )
    }
}
