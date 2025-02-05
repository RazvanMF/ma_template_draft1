package ro.kensierrat.apptemplate.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.kensierrat.apptemplate.R
import ro.kensierrat.apptemplate.activities.ui.theme.Exam2025_try2Theme
import ro.kensierrat.apptemplate.domain.GenericGrouping
import ro.kensierrat.apptemplate.domain.GenericGrouping2
import ro.kensierrat.apptemplate.server.ServerApiHelper
import ro.kensierrat.apptemplate.server.ServerBridgeCoroutine
import ro.kensierrat.apptemplate.services.RetrofitModule
import ro.kensierrat.apptemplate.views.GenericActivity1RecyclerViewAdapter
import ro.kensierrat.apptemplate.views.GenericActivity2RecyclerViewAdapter

class GenericActivity2 : ComponentActivity() {
    val data : MutableList<GenericGrouping2> = mutableListOf()
    val tempDictionary : MutableMap<String, Int> = mutableMapOf()
    val adapter = GenericActivity2RecyclerViewAdapter(data)

//    val retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:2528/") // Replace with your local server address
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    val apiService = retrofit.create(ServerApiHelper::class.java)
//    val serverBridge = ServerBridgeCoroutine(apiService)

    val RetrofitInit = RetrofitModule.getInstance()
    val serverBridge = RetrofitInit.serverBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2_layout)

        data.clear()
        prepareReturnButton()
        runtimeRecyclerInitialization()
        prepareDisplayData()
        adapter.notifyDataSetChanged()
    }

    private fun runtimeRecyclerInitialization() {
        val recyclerList = findViewById<RecyclerView>(R.id.activity2RecyclerView)
        recyclerList.layoutManager = LinearLayoutManager(this)
        recyclerList.adapter = adapter
    }

    fun prepareDisplayData() {
        serverBridge.fetchAllGenerics { response ->
            if (response == null) {
                Log.e("SERVER ERROR", "FAILED TO CONNECT TO THE SERVER FOR /allGenerics CALL")
                return@fetchAllGenerics // ???
            }

            for (generic in response) {
                val responsePrefix = generic.genericString
                if (responsePrefix in tempDictionary.keys)
                    tempDictionary[responsePrefix] = tempDictionary[responsePrefix]!! + generic.genericInt
                else
                    tempDictionary[responsePrefix] = generic.genericInt
            }

            for (key in tempDictionary.keys) {
                if (tempDictionary[key]!! > 0)
                    data.add(GenericGrouping2(key, tempDictionary[key]!!))
            }

            Log.e("sanity check", data.toString())
            Log.e("sanity check", tempDictionary.toString())
            Log.e("sanity check", response.toString())
            adapter.notifyDataSetChanged()
        }
    }

    fun prepareReturnButton() {
        val button: ImageButton = findViewById(R.id.activity2ReturnButton)
        button.setOnClickListener {
            val parentIntent: Intent = intent
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }
}