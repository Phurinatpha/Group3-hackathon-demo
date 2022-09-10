package com.example.demo

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

private const val CAMERA_CODE = 101
private var bcode : String = ""
class barcode : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_lay)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupPermissions()
        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.CONTINUOUS // or CONTINUOUS

        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW

        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {

                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_SHORT).show()
                Thread.sleep(1000)
                testapigetsku(it.text, ::getSkuSuccess)

            }
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }
    private fun getSkuSuccess(sku: String) {
        println("here get sku = " + sku)
        testapi(sku)
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequset()
        }
    }

    private fun makeRequset() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_CODE)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            CAMERA_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need hte camera permissions to use this app!" , Toast.LENGTH_LONG).show()
                }
            }
        }
    }

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
        client.newCall(request).enqueue(object : Callback {
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

    private fun testapi(sku : String ){
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
}