package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var myButton: Button
    lateinit var myText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myButton = findViewById(R.id.myButton)
        myText  = findViewById(R.id.myText)



        myButton.setOnClickListener {
            requestAPI()
        }

    }


    private fun requestAPI() {
        CoroutineScope(IO).launch {
            val data = async {
                fetchData()
            }.await()

            if (data.isNotEmpty()){
                populateRV(data)
            }
            else{
                Log.d("MAiN" , "Unable to get data")
            }

        }
    }


    private fun fetchData(): String {
        var response = ""
        try {
            response = URL("https://api.adviceslip.com/advice").readText()
        }catch (e: Exception){
            Log.d("MAIN", "ISSUE")
        }
        return response
    }

    private suspend fun populateRV(data: String) {

        withContext(Main){

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val advice = slip.getString("advice")

            myText.text = advice.toString()
        }

    }
}