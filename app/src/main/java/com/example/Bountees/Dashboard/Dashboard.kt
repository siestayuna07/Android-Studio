package com.example.Bountees.Dashboard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Bountees.AddToCart.Cart
import com.example.Bountees.AddToCart.CartActivity
import com.example.Bountees.AddToCart.DataClassNew
import com.example.Bountees.AddToCart.ItemAdapter


import com.example.Bountees.Product.DashboardAdapter
import com.example.Bountees.Profile.ProfilePage
import com.example.Bountees.R

class Dashboard : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var productRecyclerviewAdapter: DashboardAdapter? = null


    private var productList = mutableListOf<DataClassDashboard>()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_dashboard)

        val profileButton = findViewById<ImageView>(R.id.iv_profile)
        val cartButton = findViewById<ImageView>(R.id.iv_cart)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }
        cartButton.setOnClickListener {
            val intent = Intent (this, CartActivity::class.java)
            startActivity(intent)
        }


        productList = ArrayList()
        val items = mutableListOf(
            DataClassNew("Regular Fit Oxford shirt",R.drawable.design1,1549),
            DataClassNew("Regular Fit Textured-weave resort shirt",R.drawable.design2,1549),
            DataClassNew("Loose Fit Resort shirt",R.drawable.design3,1290),
            DataClassNew("Regular Fit Printed T-shirt",R.drawable.design4,499),
            DataClassNew("Loose Fit T-shirt",R.drawable.design5,499),
            DataClassNew("Loose Fit Resort shirt",R.drawable.design6,1290)
        )
        recyclerView = findViewById<View>(R.id.rv_dashboard) as RecyclerView
        this.productRecyclerviewAdapter = DashboardAdapter(this@Dashboard, productList)
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(this,2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = productRecyclerviewAdapter
        val cart = Cart(this)
        val adapter = ItemAdapter (items,cart)

        recyclerView!!.adapter = adapter


//        val recyclerViewNew = findViewById<RecyclerView>(R.id.rv_dashboard)
//        val adapter = ItemAdapter (items,cart)
//        recyclerViewNew.layoutManager = LinearLayoutManager(this)
//        recyclerViewNew.adapter = adapter




        productRecyclerviewAdapter!!.onItemClick = {  item ->
            val intent = Intent(this, ViewProduct::class.java)
            intent.putExtra("product", item)
            startActivity(intent)
        }
        prepareProductListData()
    }


    private fun prepareProductListData() {
        var shirt = DataClassDashboard("Regular Fit Oxford shirt",R.drawable.design1,1549,"PHP")
        productList.add(shirt)
        shirt = DataClassDashboard("Regular Fit Textured-weave resort shirt",R.drawable.design2,1549,"PHP")
        productList.add(shirt)
        shirt = DataClassDashboard("Loose Fit Resort shirt",R.drawable.design3,1290,"PHP")
        productList.add(shirt)
        shirt = DataClassDashboard("Regular Fit Printed T-shirt",R.drawable.design4,499,"PHP")
        productList.add(shirt)
        shirt = DataClassDashboard("Loose Fit T-shirt",R.drawable.design5,499,"PHP")
        productList.add(shirt)
        shirt = DataClassDashboard("Loose Fit Resort shirt",R.drawable.design6,1290,"PHP")
        productList.add(shirt)


        productRecyclerviewAdapter!!.notifyDataSetChanged()
    }
}