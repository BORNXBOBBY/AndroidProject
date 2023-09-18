package com.example.yati.global

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Base64
import com.example.yati.R
import com.example.yati.models.HomeRecords
import java.util.*
import kotlin.collections.ArrayList


object AppGlobal {
    var AppToken: String? = ""
    var RegisterToken: String? = ""
    var userid:String? = ""
    var referralUserId:String? =""
    var userFullName:String? = ""
    var loyaltyPoint:String? = "0"
    var userShortName: String? = "Hi"
    lateinit var prefValues:SharedPreferences
    var email: String? = ""
    var mobile_no: String? = ""

     var genericUserActivityList: List<HomeRecords> = ArrayList()

    fun getBaseAuthByUserNamePassword (userName:String, password:String): String {
        val credentials: String = userName.toString() + ":" + password
        return "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    }

    fun getHeaderMap(): Map<String, String?> {
        val headerMap = mutableMapOf<String, String?>()

        headerMap["Authorization"] = AppToken
        return headerMap
    }

    fun getBasicHeaderMap(): Map<String, String?> {
        val headerMap = mutableMapOf<String, String?>()
        headerMap["X-CSRF-Token"] = RegisterToken
        return headerMap
    }
    fun getMainHeaderMap(): Map<String, String?> {
        val headerMap = mutableMapOf<String, String?>()
        headerMap["Authorization"] = "Basic c2l0ZW1hbmFnZXI6c2l0ZW1hbmFnZXJAMTIz"
        return headerMap
    }

    fun getTodayDate(): String {
        var currentDate = ""
        val calender = Calendar.getInstance()

        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH) + 1
        val day = calender.get(Calendar.DAY_OF_MONTH)
        if (10.compareTo(month) > 0){
            if (10.compareTo(day) > 0){
                currentDate = "$year-0$month-0$day"
            }else {
                currentDate = "$year-0$month-$day"
            }
        }else{
            if (10.compareTo(day) > 0){
                currentDate = "$year-$month-0$day"
            }else {
                currentDate = "$year-$month-$day"
            }
        }
        return currentDate
    }

    fun getImageByType(type:String): Int {
        return when (type) {
            "Refer" -> R.drawable.type_referral
            "Bonus" -> R.drawable.type_bonus
            "Point", "Redeem" -> R.drawable.type_point
            "Coupon" -> R.drawable.type_coupon
            "Service" -> R.drawable.type_service_reminder
            else -> {
                R.drawable.type_others
            }
        }
    }

    fun GetAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }
}

