package net.purpleocean.themovieapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.purpleocean.themovieapp.R
import net.purpleocean.themovieapp.utils.inflate

class LoadingItemAdapter : ItemAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class LoadingViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_loading))
}