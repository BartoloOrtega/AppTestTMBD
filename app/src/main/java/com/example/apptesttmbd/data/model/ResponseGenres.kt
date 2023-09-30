package com.example.apptesttmbd.data.model

import com.google.gson.annotations.SerializedName

data class ResponseGenres(@SerializedName("genres" ) var genres : ArrayList<Genres> = arrayListOf())
