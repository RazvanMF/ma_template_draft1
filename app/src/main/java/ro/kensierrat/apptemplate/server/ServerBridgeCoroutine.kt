package ro.kensierrat.apptemplate.server

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ro.kensierrat.apptemplate.domain.GenericModel
import java.util.Base64
import java.util.UUID

class ServerBridgeCoroutine(private val apiService: ServerApiHelper) {
    fun fetchGenerics(onResult: (List<GenericModel>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getGenerics()

                // Check if response is null or empty
                if (response.isNullOrEmpty()) {
                    Log.e("fetchGenerics", "Server returned an empty or null list")
                    withContext(Dispatchers.Main) {
                        onResult(null)
                    }
                    return@launch
                }

                // Transform data
                val data = response.map { GenericModel(it.id, it.genericDate, it.genericInt, it.genericString) }

                // Switch to main thread to update UI
                withContext(Dispatchers.Main) {
                    onResult(data)
                }
            } catch (e: Exception) {
                Log.e("fetchGenerics", "Error fetching data from server", e)
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

    fun fetchAllGenerics(onResult: (List<GenericModel>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getAllGenerics()

                // Check if response is null or empty
                if (response.isNullOrEmpty()) {
                    Log.e("fetchGenerics", "Server returned an empty or null list")
                    withContext(Dispatchers.Main) {
                        onResult(null)
                    }
                    return@launch
                }

                // Transform data
                val data = response.map { GenericModel(it.id, it.genericDate, it.genericInt, it.genericString) }

                // Switch to main thread to update UI
                withContext(Dispatchers.Main) {
                    onResult(data)
                }
            } catch (e: Exception) {
                Log.e("fetchAllGenerics", "Error fetching data from server", e)
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

    fun postGeneric(generic: GenericModel, onResult: (GenericModel?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.postGeneric(GenericModel(-1, generic.genericDate, generic.genericInt, generic.genericString))
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        onResult(response.body())
                    }
                }
                else
                    throw Exception(response.raw().toString())
            }
            catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

    fun putGeneric(id: Int, generic: GenericModel, onResult: (GenericModel?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.putGeneric(id, GenericModel(id, generic.genericDate, generic.genericInt, generic.genericString))
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        onResult(generic)
                    }
                }
                else
                    throw Exception(response.raw().toString())
            }
            catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

    fun deleteGeneric(id: Int, onResult: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.deleteGeneric(id)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        onResult("true")
                    }
                }
                else
                    throw Exception(response.raw().toString())
            }
            catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

    fun fetchGeneric(id: Int, onResult: (GenericModel?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getGeneric(id)
                val data = GenericModel(response.id, response.genericDate, response.genericInt, response.genericString)
                withContext(Dispatchers.Main) {
                    onResult(data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }
}