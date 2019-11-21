package net.purpleocean.themovieapp.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_recycler.*
import net.purpleocean.themovieapp.R
import net.purpleocean.themovieapp.data.MovieList
import net.purpleocean.themovieapp.ui.adapter.MovieAdapter
import net.purpleocean.themovieapp.utils.androidLazy
import net.purpleocean.themovieapp.utils.inflate

class MovieFragment : RxBaseFragment() {

    private val movieManager by lazy { MovieManager() }
    private var theMovieList: MovieList? = null
    private val movieAdapter by androidLazy { MovieAdapter() }

    companion object {
        private val KEY_THE_MOVIE = "theMoviePopular"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.frag_recycler)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) { // (2)
        super.onActivityCreated(savedInstanceState)
        rv_movie_list.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ requestMovie() }, linearLayout))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rv_movie_list.layoutManager = LinearLayoutManager(context)
        }

        if (rv_movie_list.adapter == null) {
            rv_movie_list.adapter = movieAdapter
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_THE_MOVIE)) {
            theMovieList = savedInstanceState.get(KEY_THE_MOVIE) as MovieList
            movieAdapter.clearAndAddMovieList(theMovieList!!.results)
        } else {
            requestMovie()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val movie = movieAdapter.getMovieList()
        if (theMovieList != null && movie.isNotEmpty()) {
            outState.putParcelable(KEY_THE_MOVIE, theMovieList?.copy(results = movie))
        }
    }

    private fun requestMovie() {
        val subscription = movieManager.getMovieList((theMovieList?.page).toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { retrievedMovie ->
                    (rv_movie_list.adapter as MovieAdapter).addMovieList(retrievedMovie.results)
                }, { e ->
                    Snackbar.make(rv_movie_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                })
        subscriptions.add(subscription)
    }
}
