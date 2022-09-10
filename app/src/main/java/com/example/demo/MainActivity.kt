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
import com.google.gson.Gson
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.chromium.base.Promise
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        testapigetsku("2502009951898", ::getSkuSuccess)


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

    private fun getSkuSuccess(sku: String) {
        println("here get sku =" + sku)
        testapi(sku)
    }

    private fun testpostapi(){
        val payload = "test payload"
        println("tesssssssstttttttttttt")
        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("https://data.mongodb-api.com/app/data-swrdf/endpoint/data/v1/action/findOne")
            .addHeader("Content-Type", "application/json")
            .addHeader("Access-Control-Request-Headers", "*")
            .addHeader("api-key", "ileFMdqL4lKjPcWHlWMLjkHlB7vCqGN0hfS8ZWqqGyRUN1VYPr3ILzQqjzaWVUu5")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                print("not in faillerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr")
            }
            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val tex = response.body?.string()
                //val model = gson.fromJson(tex, Model::class.java)
                if (tex != null) {
                    println("posttttttttttttttttttttttttttttttttttttt")
                    println(tex)
                }else {
                    println(tex)
                }
                println("posttttttttttttttttttttttttttttttttttttt")
            }
        })
    }

    private fun testapi(sku : String ){
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
        var url_data = "https://ppe-api.lotuss.com/proc/product/api/v1/products/details?websiteCode=thailand_hy&storeId=5016&sku="+sku
        val request = Request.Builder()
            .url(url_data)
            .addHeader("accept-language", "th")
            .addHeader("Authorization", "Basic ZWY4ZTZjMjgzODdlNGVjYTlkM2UxMTU1MDQxMjgyYzE6MEU1NTg0QzUxZTdBNDBEODkzMDUxZGExY2NEQTg2ZTY=")
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response){
                val gson = Gson()
                val tex = response.body?.string()
                val testModel = gson.fromJson(tex, TestModel::class.java)
                if (testModel != null) {
                    println(testModel)
                }else {
                    println(tex)
                }
            }

        })
    }

//    private fun testapigetcustomer(){
//        var str_pnum = "66810000001"
//        var url_phone = "https://script.google.com/macros/s/AKfycbwBJ4REN3fhSb1_KSDMATnGXUHzHMmt9Wh-xHFViLqhzk4Lac3xDLlnZKtPBK1lTzJd/exec?topic=customer&data="+str_pnum
//        val request = Request.Builder()
//            .url(url_phone)
//            .build()
//        client.newCall(request).enqueue(object : Callback{
//            override fun onFailure(call: Call, e: IOException) {}
//            override fun onResponse(call: Call, response: Response){
//                val tex = response.body?.string()
////                val gson = Gson()
////
////                val testModel = gson.fromJson(tex, TestModel::class.java)
//                if (tex != null) {
//                    println(tex)
//                }else {
//                }
//            }
//
//        })
//    }

    private fun testapigetsku(str_bar: String, callback: (String) -> Unit) {
        data class Model(
            val barcode: String,
            val sku : String
        )
        var sku_u : String
        var url_bar = "https://script.google.com/macros/s/AKfycbwBJ4REN3fhSb1_KSDMATnGXUHzHMmt9Wh-xHFViLqhzk4Lac3xDLlnZKtPBK1lTzJd/exec?topic=barcode&data="+str_bar
        val request = Request.Builder()
            .url(url_bar)
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response)  {
                val tex = response.body?.string()
                val gson = Gson()
                val model = gson.fromJson(tex, Model::class.java)
                if (model != null) {
                    //println(model)
                    sku_u = model.sku
                    callback(sku_u)
                }
            }

        })
    }

}

