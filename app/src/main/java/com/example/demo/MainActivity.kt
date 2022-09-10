package com.example.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.demo.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.util.Assert
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.io.StringBufferInputStream
import java.util.*
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        testapi()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_profile,
                R.id.navigation_cart, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val f : FloatingActionButton = binding.fab
        f.setOnClickListener {
            val intent = Intent(this, barcode::class.java)
            startActivity(intent)
        }

    }

    private fun testapi(){
        data class  status(
            val code : Int,
            val message : String
        )

        data class list_img(
            val url: String
        )
        data class regularPrice(
            val value :String,
            val currency : String
        )

        data class  finalPrice(
            val value :String,
            val currency : String
        )

        data class discount(
            val amountOff : String,
            val percentOff : String
        )
        data class minimumPrice(
            val regularPrice : regularPrice,
            val finalPrice : finalPrice,
            val discount : discount
        )

        data class promotion_url(
            val image : String
        )

        data class Detail(
            val title : String,
            val content : String
        )

        data class Data(
            val id : Int,
            val sku : String,
            val name : String,
            val mediaGallery : Array<list_img>,
            val priceRange : minimumPrice,
            val promotions : Array<promotion_url>,
            val tabs : Array<Detail>

        )


        data class TestModel(
            val status: status,
            val data: Data
        )

        val request = Request.Builder()
            //.url("https://ppe-api.lotuss.com/proc/product/api/v1/products/details?websiteCode=thailand_hy&storeId=5016&sku=51202528%22")
            .url("https://ppe-api.lotuss.com/proc/product/api/v1/products/details?websiteCode=thailand_hy&storeId=5016&sku=51202528")
            .addHeader("accept-language", "th")
            .addHeader("Authorization", "Basic ZWY4ZTZjMjgzODdlNGVjYTlkM2UxMTU1MDQxMjgyYzE6MEU1NTg0QzUxZTdBNDBEODkzMDUxZGExY2NEQTg2ZTY=")
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response){
                var gson = Gson()
                var tex = response.body?.string()
                var testModel = gson.fromJson(tex, TestModel::class.java)
                if (testModel != null) {
                    println("hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")

                    println(testModel)
                }else {
                    println(tex)
                }
                println("hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")

            }

        })
    }
}

