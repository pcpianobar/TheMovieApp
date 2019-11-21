package net.purpleocean.themovieapp.ui.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import net.purpleocean.themovieapp.data.MovieItem

class MovieAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> () {

    private var items: ArrayList<ViewType>  // (1)
    private var delegateAdapters = SparseArrayCompat<ItemAdapter>() // (2)
    private val loadingItem = object : ViewType {   // (3)
        override fun getViewType() = AdapterType.LOADING
    }

    // (4)
    init {
        delegateAdapters.put(AdapterType.LOADING, LoadingItemAdapter())
        delegateAdapters.put(AdapterType.MOVIE, MovieItemAdapter())
        items = ArrayList()
        items.add(loadingItem)
    }

    // (5)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapters.get(viewType)!!.onCreateViewHolder(parent)

    // (6)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegateAdapters.get(getItemViewType(position))!!.onBindViewHolder(holder, items[position])

    override fun getItemCount(): Int = items.size

    // (7)
    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun addMovieList(movieList: List<MovieItem>) {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        items.addAll(movieList)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun clearAndAddMovieList(movieList: List<MovieItem>) {
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(movieList)
        items.add(loadingItem)
        notifyItemRangeInserted(0, items.size)
    }

    fun getMovieList(): List<MovieItem> = items
        .filter { it.getViewType() == AdapterType.MOVIE }
        .map { it as MovieItem }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex
}