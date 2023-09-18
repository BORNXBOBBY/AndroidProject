package com.example.yati.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yati.R
import com.example.yati.global.AppGlobal
import com.example.yati.models.ProductsRecords
import kotlinx.android.synthetic.main.productlist_item.view.*

class ProductsAdapter(var products:List<ProductsRecords>, private  val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.productlist_item,parent,false)

        return ProductViewHolder(view, onItemClickListener)
    }

    override fun getItemCount() = products.size
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product= products[position]

        holder.itemContent = product
        holder.view.productScooter_textView.text ="Yamaha " + product.name
        holder.view.price_textView.text = product.price
        holder.view.scooter_mileage_textView.text = product.mileage
        holder.view.scooter_enginePower_textView.text = product.engine_power
        holder.view.scooter_powerUnit_textView.text = product.unit_power
        holder.view.scooter_gravity_textView.text = product.gravity
        Glide.with(holder.view.context)
            .load(product.image)
            .into(holder.view.scooter_imageView)

        if (AppGlobal.prefValues.contains(product.document)) {
            holder.view.favorite.setImageResource(R.drawable.ic_baseline_favorite_red)
        } else {
            holder.view.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    class ProductViewHolder(val view: View,onItemClickListener: OnItemClickListener): RecyclerView.ViewHolder(view){
        lateinit var itemContent:ProductsRecords

        init {

            view.add_bike_btn.setOnClickListener {
                onItemClickListener.OnAddBikeClick(itemContent)
            }

            view.book_now_btn.setOnClickListener {
                onItemClickListener.OnBookNowClick(itemContent)
            }

            view.favorite.setOnClickListener {
                onItemClickListener.OnFavClick(itemContent, view)
            }
        }
    }
}

interface OnItemClickListener {
    fun OnAddBikeClick(contentItem: ProductsRecords)
    fun OnBookNowClick(contentItem: ProductsRecords)
    fun OnFavClick(contentItem: ProductsRecords, view:View)
}