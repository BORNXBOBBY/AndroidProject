package com.example.threadcorotinesapicall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.example.mockableapi.Apicalling
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val tag = "Bobby"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onStart() {
        super.onStart()
        Apicalling.create().getUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                CoroutineScope(Dispatchers.IO).launch {
                    result.forEach { it ->
                        Log.d(tag, "${it.name},${it.email}")
                    }
                }
            }
        fun getBaseAuthByUserNamePassword (userName:String, password:String): String {
            val credentials: String = userName +":"+ password
            return "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        }

        fun getHeaderMap(authKey:String): Map<String, String?>{
            val headerMap = mutableMapOf<String,String?>()

            headerMap["Authoriazation"] = authKey
            return headerMap
        }
    }
}