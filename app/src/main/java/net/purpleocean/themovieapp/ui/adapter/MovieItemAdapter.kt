package net.purpleocean.themovieapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_movie.view.*
import net.purpleocean.themovieapp.data.MovieItem
import net.purpleocean.themovieapp.utils.inflate
import net.purpleocean.themovieapp.utils.loadImg
import net.purpleocean.themovieapp.R

class MovieItemAdapter(val viewActions: ViewSelectedListener) : ItemAdapter {

    interface ViewSelectedListener {
        fun onItemSelected(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return MovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as MovieViewHolder
        holder.bind(item as MovieItem)
    }

    // 이너 클래스에서는 바깥 클래스의 프로퍼티 등을 접근 할 수 있다.
    inner class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.item_movie)) {

        private val imgPoster = itemView.img_poster
        private val overview = itemView.tv_overview
        private val releaseDate = itemView.tv_release_date
        private val voteCount = itemView.tv_vote_count
        private val tvTitle = itemView.tv_title
        private val tvAverage = itemView.rate_vote_avg
        private val btnReserve = itemView.btn_reserve

        fun bind(item: MovieItem) { // (5)
            imgPoster.loadImg("https://image.tmdb.org/t/p/w500/${item.poster_path}")
            overview.text = item.overview
            releaseDate.text = item.release_date
            voteCount.text = "${item.vote_count} 투표"
            tvTitle.text = item.title
            tvAverage.rating = item.vote_average / 2  // (7)

            super.itemView.setOnClickListener {
                viewActions.onItemSelected("https://image.tmdb.org/t/p/w500/${item.poster_path}")
            }

            btnReserve.setOnClickListener {
                Snackbar.make(it, "스낵바입니다.", Snackbar.LENGTH_LONG)
            }
        }
    }
}