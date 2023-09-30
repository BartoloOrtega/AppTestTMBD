package com.example.apptesttmbd.ui.activity.listmovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.apptesttmbd.BuildConfig
import com.example.apptesttmbd.R
import com.example.apptesttmbd.data.api.ApiClient
import com.example.apptesttmbd.data.api.ApiService
import com.example.apptesttmbd.data.model.Content
import com.example.apptesttmbd.data.model.Movie
import com.example.apptesttmbd.databinding.ActivityListMoviesBinding
import com.example.apptesttmbd.databinding.ActivityMainBinding
import com.example.apptesttmbd.ui.activity.detailmovies.DetailMoviesActivity
import com.example.apptesttmbd.ui.activity.login.MainActivity
import com.example.apptesttmbd.ui.adapter.MovieAdapter
import com.example.apptesttmbd.utils.customDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListMoviesBinding
    var numPage: Int=1
    var userName: String=""
    private var apiInterface = ApiClient.getClient()?.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName= getString(R.string.msgWelcome,intent.getStringExtra("user").toString())
        supportActionBar?.setSubtitle(userName)
        getMovies()

        binding.container.setOnRefreshListener {
            numPage++
            getMovies()
        }
    }

    fun getMovies(){
        try{}catch (ex: Exception){
            customDialog.instance?.DialogSimple(this, ex.message)
        }
        customDialog.instance?.showWaitDialog(this, null)
        GlobalScope.launch {
            apiInterface!!.getNowPlayin(BuildConfig.TOKEN,"application/json", numPage).enqueue(object:
                Callback<Content> {
                override fun onResponse(call: Call<Content>, response: Response<Content>) {
                    //TODO("Not yet implemented")
                    var responses= response.body();
                    fillData(responses!!.results)
                    Log.d("GetMovies", numPage.toString())
                    customDialog.instance?.closeWaitdialog(this@ListMoviesActivity)
                    binding.container.isRefreshing=false
                    binding.textViewPage.text=numPage.toString()

                }

                override fun onFailure(call: Call<Content>, t: Throwable) {
                    customDialog.instance?.DialogSimple(this@ListMoviesActivity, getString(R.string.error_get_movies))
                    customDialog.instance?.closeWaitdialog(this@ListMoviesActivity)
                }
            })
        }
    }
    lateinit var moviesAdapter: MovieAdapter
    fun fillData(MovieLists: ArrayList<Movie>){
        try{
            moviesAdapter = MovieAdapter(MovieLists, this)
            binding.RecycleMovies.adapter = moviesAdapter
            moviesAdapter.onItemClick={ item->
                //val bundle = Bundle()
                // bundle.putString("project_id", item.text)
                val detailmovies= Intent(this, DetailMoviesActivity::class.java)
                detailmovies.putExtra("idmovie",item.id)
                startActivity(detailmovies)
            }
            moviesAdapter.notifyDataSetChanged()
            if(numPage==1)
                customDialog.instance?.DialogSimple(this,userName )
        }catch (ex: Exception){
            customDialog.instance?.DialogSimple(this, ex.message)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if(id== R.id.logout) {
            customDialog.instance?.DialogMessageConfirm(this,getString(R.string.logout), getString(R.string.logout_q), "",true,{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            })

        }

        return super.onOptionsItemSelected(item)
    }
}