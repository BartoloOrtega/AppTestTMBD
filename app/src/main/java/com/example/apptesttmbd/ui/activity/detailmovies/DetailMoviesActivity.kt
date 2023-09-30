package com.example.apptesttmbd.ui.activity.detailmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.example.apptesttmbd.BuildConfig
import com.example.apptesttmbd.R
import com.example.apptesttmbd.data.api.ApiClient
import com.example.apptesttmbd.data.api.ApiService
import com.example.apptesttmbd.data.model.ResponseGenres
import com.example.apptesttmbd.data.model.ResponseMovieDetail
import com.example.apptesttmbd.databinding.ActivityDetailMoviesBinding
import com.example.apptesttmbd.utils.customDialog
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMoviesBinding
    private var apiInterface = ApiClient.getClient()?.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        val id2= intent.getIntExtra("idmovie",0)
        getDetailMovie()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }
    fun getDetailMovie(){
        try{
            customDialog.instance?.showWaitDialog(this, null)
            GlobalScope.launch {

                apiInterface!!.getMovieDetail(BuildConfig.TOKEN,"application/json", intent.getIntExtra("idmovie",0))?.enqueue(object:
                    Callback<ResponseMovieDetail> {
                    override fun onResponse(call: Call<ResponseMovieDetail>, response: Response<ResponseMovieDetail>) {

                        // var responses= response.body();
                        response.body()?.let { fillMovie(it) }
                        customDialog.instance?.closeWaitdialog(this@DetailMoviesActivity)

                    }

                    override fun onFailure(call: Call<ResponseMovieDetail>, t: Throwable) {

                        customDialog.instance?.closeWaitdialog(this@DetailMoviesActivity)
                        customDialog.instance?.DialogMessageConfirm(this@DetailMoviesActivity,"Error al obtener información",getString(R.string.error_get_detail_movie),"OK",false,{
                            finish()
                        })
                    }
                })
            }

        }catch (ex: Exception){
            customDialog.instance?.DialogSimple(this, ex.message)
        }
    }
    fun fillMovie(movie: ResponseMovieDetail){
        try{
            Picasso.get()
                .load("https://image.tmdb.org/t/p/original"+ movie.posterPath)
                .fit()

                .placeholder(R.drawable.ic_baseline_local_movies_24)
                //  .resize(150, 150)
                .centerInside()

                .into(binding.imageViewCover)


            binding.textViewTitle.setText(movie.title)
            binding.textViewDuracion.text= movie.runtime.toString().plus(" Min.")
            binding.textViewFechaEstreno.text= movie.releaseDate
            binding.textViewDescripcion.text=movie.overview
            binding.switch1.isChecked= movie.adult == true
            binding.ratingBar.rating= movie.voteAverage!!.toFloat()
            binding.textViewGeneros.text=""
            movie.genres.forEach {
                binding.textViewGeneros.text=binding.textViewGeneros.text.toString()+ it.name + "/"

            }
        }catch (ex: Exception){
            customDialog.instance?.DialogSimple(this, ex.message)
        }
    }
    fun getGenres(){
        try{
            GlobalScope.launch {
                apiInterface!!.getGenre(BuildConfig.TOKEN,"application/json").enqueue(object:
                    Callback<ResponseGenres> {
                    override fun onResponse(call: Call<ResponseGenres>, response: Response<ResponseGenres>) {
                        //TODO("Not yet implemented")
                        var responses= response.body();

                        customDialog.instance?.closeWaitdialog(this@DetailMoviesActivity)
                    }
                    override fun onFailure(call: Call<ResponseGenres>, t: Throwable) {
                        //TODO("Not yet implemented")
                    }
                })
            }
        }catch (ex: Exception){
            customDialog.instance?.DialogSimple(this, ex.message)
        }
    }
}