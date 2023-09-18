package com.example.yati.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yati.R
import com.example.yati.activities.AddBikeActivity
import com.example.yati.adapters.OnItemClickListener
import com.example.yati.adapters.ProductsAdapter
import com.example.yati.global.AppGlobal
import com.example.yati.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.productlist_item.view.*
import kotlinx.android.synthetic.main.products_fragment.*

class ProductsFragment : YatiFragment() ,OnItemClickListener {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private var productsList : List<ProductsRecords> = ArrayList()
    private var productsAdapter:ProductsAdapter = ProductsAdapter(productsList,this)

    companion object {
        fun newInstance() = ProductsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.products_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isOnline()){
            displayLoader()
            loadProducts()
        }else{
            showAlertDialog()
        }
    }

    private fun loadProducts(){
        firestore.collection("products")
            .get()
            .addOnCompleteListener {
                productsList = it.result!!.toObjects(ProductsRecords::class.java)
                productsAdapter.products = productsList
                productsAdapter.notifyDataSetChanged()
                renderProduct()
                hieLoader()
            }
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(activity, "Product loading failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun renderProduct(){
        productsrecyclerView.layoutManager= LinearLayoutManager(this.context)
        productsrecyclerView.adapter= productsAdapter
        
    }

    override fun OnAddBikeClick(contentItem: ProductsRecords) {
        val intent = Intent(activity,AddBikeActivity::class.java)
        intent.putExtra("productName", contentItem.name)
        intent.putExtra("productImage", contentItem.image)
        intent.putExtra("productId", contentItem.document)

        startActivity(intent)
    }

    override fun OnBookNowClick(contentItem: ProductsRecords) {
        if (isOnline()){
            displayLoader()
            requestBooking(contentItem)
        }else{
            showAlertDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun OnFavClick(contentItem: ProductsRecords, view: View) {

        val editor = AppGlobal.prefValues.edit()
        if (AppGlobal.prefValues.contains(contentItem.document)) {
            editor.remove(contentItem.document).apply()
            view.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        } else {
            editor.putString(contentItem.document, "true").apply()
            view.favorite.setImageResource(R.drawable.ic_baseline_favorite_red)
            checkForFav(contentItem)
        }
    }

    @SuppressLint("CheckResult")
    private fun requestBooking(contentItem: ProductsRecords) {
        val requestBooking: MutableMap<String, Any> = HashMap()
        requestBooking["enquiry_type"] = "Booking"
        requestBooking["product_name"] = contentItem.name.toString()
        requestBooking["product_id"] = contentItem.document.toString()
        requestBooking["user_id"] = auth.currentUser?.uid.toString()
        requestBooking["user_name"] = AppGlobal.userFullName.toString()
        requestBooking["user_phone"] = AppGlobal.mobile_no.toString()
        requestBooking["user_email"] = AppGlobal.email.toString()
        requestBooking["booking_requested_date"] = AppGlobal.getTodayDate().toString()
        requestBooking["booking_delivered_date"] = ""

        firestore.collection("Bookings")
            .add(requestBooking)
            .addOnCompleteListener {
                showBookingAlertSuccessfully()
                hieLoader()
            }
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(activity, "Booking Failed", Toast.LENGTH_SHORT).show()
                showBookingAlertDialog()
                hieLoader()
            }
    }

    private fun loadFavorite(documentToken: String, name :String){

        val favRequest = Favorite("interested" , AppGlobal.userid, documentToken, name, "true".toBoolean())

            firestore.collection("Enquiries")
                .add(favRequest)
                .addOnSuccessListener {
                    val favPreferences :SharedPreferences = activity?.getSharedPreferences("fav_pref", Context.MODE_PRIVATE) !!
                       favPreferences.edit().putString("favorite",documentToken)
                           .apply()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show()
                }
    }

    fun showAlertDialog(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                displayLoader()
                loadProducts()
                Toast.makeText(activity,"Your are Online!", Toast.LENGTH_SHORT).show()
            }else{
                showAlertDialog()
                Toast.makeText(activity,"Please Go Online!", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("Cancel"){dialog, id ->
            dialog.dismiss()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()

        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)

    }

    private fun showBookingAlertDialog(){
        val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        alertDialog.setTitle("Connection Problem")
        alertDialog.setMessage("Please check your Connection!")
        alertDialog.setIcon(R.drawable.ic_baseline_warning)
        alertDialog.setPositiveButton("Try Again"){ dialog, id ->

            if (isOnline()){
                Toast.makeText(activity,"Your are Online!", Toast.LENGTH_SHORT).show()
            }else{
                showBookingAlertDialog()
                Toast.makeText(activity,"Please Go Online!", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.setNegativeButton("Cancel"){dialog, id ->
            dialog.dismiss()
        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()

        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.RED)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(Color.GREEN)

    }
    private fun checkForFav(product: ProductsRecords){
        firestore.collection("Enquiries")
            .whereEqualTo("user", AppGlobal.userid)
            .whereEqualTo("product", product.document)
            .whereEqualTo("type", "interested")
            .get()
            .addOnSuccessListener {
                if (it.documents.size == 0) {
                    loadFavorite(product.document.toString(), product.name.toString())
                }
            }
            .addOnFailureListener {
              it.printStackTrace()
            }
    }
    private fun showBookingAlertSuccessfully(){
        val bookingAlert = AlertDialog.Builder(activity)
        bookingAlert.setTitle("Booking Successfully.")
        bookingAlert.setMessage("Thanks we will call you soon.")
        bookingAlert.setIcon(R.drawable.type_success)
        bookingAlert.setPositiveButton("Ok"){dialog, id ->
        }

        val alertDialog = bookingAlert.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }
}