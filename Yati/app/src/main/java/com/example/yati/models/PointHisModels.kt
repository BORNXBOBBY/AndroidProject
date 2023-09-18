package com.example.yati.models

data class PointRecord(
    val user_id : String? = null,
    val title : String? = null,
    val description : String? = null,
    val points : Long? = null,
    val transaction_number : Long? = null,
    val transaction_type : String? = null,
    val date : String? = null
)

