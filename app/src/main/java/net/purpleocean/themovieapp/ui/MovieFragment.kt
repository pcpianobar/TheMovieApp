package net.purpleocean.themovieapp.ui

import android.content.Intent
import android.net.Uri
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.purpleocean.themovieapp.R
import net.purpleocean.themovieapp.data.API_KEY
import net.purpleocean.themovieapp.data.MovieList
import net.purpleocean.themovieapp.ui.adapter.MovieAdapter
import net.purpleocean.themovieapp.ui.adapter.MovieItemAdapter
import net.purpleocean.themovieapp.utils.androidLazy
import net.purpleocean.themovieapp.utils.inflate
import org.jetbrains.anko.design.snackbar

class MovieFragment : RxBaseFragment(), MovieItemAdapter.ViewSelectedListener {

    private val movieManager by lazy { MovieManager() }
    private var theMovieList: MovieList? = null
    private val movieAdapter by androidLazy { MovieAdapter(this) }

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
            val linearLayout = LinearLayoutManager(this.context)
            layoutManager = linearLayout
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ requestMovie() }, linearLayout))
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
//        val subscription = movieManager.getMovieList((theMovieList?.page).toString())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { retrievedMovie ->
//                    (rv_movie_list.adapter as MovieAdapter).addMovieList(retrievedMovie.results)
//                }, { e ->
//                    Snackbar.make(rv_movie_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
//                })
//        subscriptions.add(subscription)

        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                val param = mapOf(
                    "page" to (theMovieList?.page).toString(),
                    "api_key" to API_KEY,
                    "sort_by" to "popularity.desc",
                    "language" to "ko"
                )
                val retrievedMovie = movieManager.getMovieList(param)
                retrievedMovie.page = retrievedMovie.page?.plus(1)

                theMovieList = retrievedMovie
                movieAdapter.addMovieList(retrievedMovie.results)
            } catch (e: Throwable) {
                if (isVisible) {
                    rv_movie_list.snackbar(e.message.orEmpty(), "RETRY") { requestMovie() }
                }
            }
        }
    }

    override fun onItemSelected(url: String?) {
        if (url.isNullOrEmpty()) {
            rv_movie_list.snackbar("No URL assigned to this results")
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}
