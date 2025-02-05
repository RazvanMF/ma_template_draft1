package ro.kensierrat.apptemplate.services

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.kensierrat.apptemplate.server.ServerApiHelper
import ro.kensierrat.apptemplate.server.ServerBridgeCoroutine

class RetrofitModule private constructor() {
    companion object {
        @Volatile
        private var instance : RetrofitModule? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RetrofitModule().also { instance = it }
            }
    }

    public val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:2528/") // Replace with your local server address
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    public val apiService = retrofit.create(ServerApiHelper::class.java)
    public val serverBridge = ServerBridgeCoroutine(apiService)
}