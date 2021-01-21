package com.example.androidkotlin.ui.feeds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlin.R

class FeedsAdapter(val list: ArrayList<String>): RecyclerView.Adapter<FeedsAdapter.FeedViewHolder>() {
    class FeedViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        private val nameView: TextView = itemView.findViewById(R.id.feeds_name)
        fun bind(word:String){
            nameView.text=word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.feed_layout,parent,false);
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(list.get(position));
    }

    override fun getItemCount(): Int {
        return list.size
    }


}