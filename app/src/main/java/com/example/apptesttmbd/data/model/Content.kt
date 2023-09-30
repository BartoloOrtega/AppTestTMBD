package com.example.apptesttmbd.data.model

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("dates"         ) var dates        : Dates?             = Dates(),
    @SerializedName("page"          ) var page         : Int?               = null,
    @SerializedName("results"       ) var results      : ArrayList<Movie> = arrayListOf(),
    @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
    @SerializedName("total_results" ) var totalResults : Int?               = null
)
