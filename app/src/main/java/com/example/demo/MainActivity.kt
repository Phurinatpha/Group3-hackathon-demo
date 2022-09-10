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



}

