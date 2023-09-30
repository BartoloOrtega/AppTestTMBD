package com.example.apptesttmbd.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptesttmbd.R
import com.example.apptesttmbd.data.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(private val movieList: ArrayList<Movie>,
                   private val context: Context) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    public var onItemClick: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_movie,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        // holder.imageViewPicture.image=
        if(!movieList.get(position).backdropPath.isNullOrEmpty()){
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500"+ movieList.get(position).backdropPath)
                .fit()
                .placeholder(R.drawable.ic_baseline_camera_24)
                //.resize(150, 150)
                .centerInside()
                .into(holder.imageViewPicture)
        }
        holder.movieNameTV.text = movieList.get(position).title
        holder.ratin.setRating(movieList.get(position).voteAverage!!.toFloat());
    }

    override fun getItemCount(): Int {
        // on below line we are returning
        // our size of our list
        return movieList.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewPicture: ImageView = itemView.findViewById(R.id.imageViewPicture)
        val movieNameTV: TextView = itemView.findViewById(R.id.textViewName)
        val ratin: RatingBar = itemView.findViewById(R.id.ratingBar)
        init {
            itemView.setOnClickListener({
                onItemClick?.invoke(movieList[adapterPosition])
            })
        }
    }
}