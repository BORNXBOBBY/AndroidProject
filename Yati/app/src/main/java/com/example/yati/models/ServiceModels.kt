package com.example.yati.models

data class ServiceModels(
    //val service_nid :String,
    val bike_name:String? = null,
    val problem_in_bike :String? = null,
    val delivered_date_time :String? = null,
    val requested_date_time :String? = null,
    val status :String? = null,
    val user_id :String? = null,
    val rating : Int? = null,
    val id: String? = null,
    val feedback : String? = null

)