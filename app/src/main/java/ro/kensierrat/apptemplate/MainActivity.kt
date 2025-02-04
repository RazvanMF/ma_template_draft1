package ro.kensierrat.apptemplate

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.kensierrat.apptemplate.activities.AddActivity
import ro.kensierrat.apptemplate.activities.GenericActivity1
import ro.kensierrat.apptemplate.activities.GenericActivity2
import ro.kensierrat.apptemplate.domain.GenericModel
import ro.kensierrat.apptemplate.server.DBHelper
import ro.kensierrat.apptemplate.server.ServerApiHelper
import ro.kensierrat.apptemplate.server.ServerBridgeCoroutine
import ro.kensierrat.apptemplate.views.GenericRecyclerViewAdapter

class MainActivity : ComponentActivity() {
    var data = mutableListOf<GenericModel>()
    val adapter = GenericRecyclerViewAdapter(data)

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:2528/") // Replace with your local server address
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ServerApiHelper::class.java)
    val serverBridge = ServerBridgeCoroutine(apiService)

    val db = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_layout)

        data.clear()
        runtimeRecyclerInitialization()
        runtimeActivitySwitch()
        retrieveFromServer()

        adapter.notifyDataSetChanged()
    }

    fun runtimeActivitySwitch() {
        val activity1button: ImageButton = findViewById(R.id.genericItemActivity1Button)
        val launcherForActivity1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == Activity.RESULT_OK || it.resultCode == Activity.RESULT_CANCELED) {
                Log.i("ui interaction info", "SUCCESSFULLY RETURNED FROM ACTIVITY 1")
            }
        }
        activity1button.setOnClickListener {
            Log.i("ui interaction info", "OPENED ACTIVITY 1 FROM MAIN")
            val intent: Intent = Intent(this, GenericActivity1::class.java)
            launcherForActivity1.launch(intent)
        }

        val activity2button: ImageButton = findViewById(R.id.genericItemActivity2Button)
        val launcherForActivity2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == Activity.RESULT_OK) {
                Log.i("ui interaction info", "SUCCESSFULLY RETURNED FROM ACTIVITY 2")
            }
        }
        activity2button.setOnClickListener {
            Log.i("ui interaction info", "OPENED ACTIVITY 2 FROM MAIN")
            val intent: Intent = Intent(this, GenericActivity2::class.java)
            launcherForActivity2.launch(intent)
        }
    }

    fun retrieveFromServer() {
        serverBridge.fetchGenerics { genericsList ->
            runOnUiThread { // Ensure UI updates happen on the main thread
                data.clear() // Clear old data only AFTER getting new data
                if (genericsList != null) {
                    data.addAll(genericsList)
                    db.nukeDB()
                    for (generic in genericsList) {
                        db.addGeneric(generic)
                    }
                    adapter.notifyDataSetChanged() // Ensure RecyclerView updates
                } else {
                    showFetchErrorDialog()
                }
            }
        }
    }

    private fun showFetchErrorDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("The fetch operation from the server has failed. What do you want to do next?")
            .setCancelable(false)
            .setPositiveButton("RETRY") { dialog, id ->
                retrieveFromServer()
            }
            .setNeutralButton("USE LOCAL DB") { dialog, id ->
                data.addAll(db.getAllGenerics()) // Add offline data
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("EXIT") { dialog, id ->
                finish()
            }
            .setTitle("FETCH OPERATION FAILED")
            .create()
            .show()
    }

    private fun runtimeRecyclerInitialization() {
        val recyclerList = findViewById<RecyclerView>(R.id.genericItemRecyclerViewList)
        recyclerList.layoutManager = LinearLayoutManager(this)
        recyclerList.adapter = adapter

        adapter.setOnDeleteButtonPress(object : GenericRecyclerViewAdapter.OnClickListener {
            override fun onClick(position: Int, model: GenericModel, id: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Are you sure you want to delete this GENERIC ITEM with ID #\"${model.id}\"?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        data.removeAt(position)
                        serverBridge.deleteGeneric(model.id) { response ->
                            Log.i("self", response.toString())
                        }
                        Toast.makeText(
                            this@MainActivity,
                            "SUCCESSFULLY DELETED GENERIC ITEM #\"${model.id}\"",
                            Toast.LENGTH_SHORT
                        ).show()
                        adapter.notifyItemRemoved(position)
                    }
                    .setNegativeButton("No") {dialog, id ->
                        dialog.dismiss()
                    }
                    .setTitle("PROMPT")
                    .create()
                    .show()
            }
        })

        adapter.setOnInfoButtonPress(object : GenericRecyclerViewAdapter.OnClickListener {
            override fun onClick(position: Int, model: GenericModel, id: Int) {
                Log.i("lmao", "OPENED INFO PAGE")
                serverBridge.fetchGeneric(model.id) { response ->
                    if (response != null) {
                        val builder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("GENERIC #${response.id}\n\n\tDATE: ${response.genericDate}\n\tINT: ${response.genericInt}\n\tSTRING: ${response.genericString}")
                            .setCancelable(false)
                            .setNeutralButton("OK", { dialog, id ->
                                //comment this out if nu a vrut autorul asta
                                if (db.getComplexGeneric(response.id).id == -1)
                                    db.addComplexGeneric(response)
                                dialog.dismiss()
                            })
                            .setTitle("PROMPT FROM SERVER")

                        val alert = builder.create()
                        alert.show()
                    }
                    else {
                        // val failsafe = db.getGeneric(model.id)
                        val failsafe = db.getComplexGeneric(model.id)
                        val builder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("GENERIC #${failsafe.id}\n\n\tDATE: ${failsafe.genericDate}\n\tINT: ${failsafe.genericInt}\n\tSTRING: ${failsafe.genericString}")
                            .setCancelable(false)
                            .setNeutralButton("OK", { dialog, id ->
                                dialog.dismiss()
                            })
                            .setTitle("PROMPT FROM LOCAL")

                        val alert = builder.create()
                        alert.show()
                    }
                }
            }
        })

        // the global add button
        val addbutton: ImageButton = findViewById(R.id.genericItemAddButton)
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data;
                val newGeneric = data?.getParcelableExtra("COMPONENT_RETURNED", GenericModel::class.java)

                if (newGeneric != null) {
                    var resp: GenericModel
                    serverBridge.postGeneric(newGeneric) {response ->
                        this.data.add(response!!)
                        adapter.notifyItemInserted(this.data.size - 1)
                    }

                    Toast.makeText(this, "SUCCESSFULLY ADDED NEW GENERIC", Toast.LENGTH_SHORT).show()
                    Log.i("lmao", "SUCCESSFULLY UPLOADED NEW ALBUM")
                }
            }
        }
        addbutton.setOnClickListener {
            Log.i("lmao", "OPENED ADD PAGE")
            val intent: Intent = Intent(this, AddActivity::class.java)
            launcher.launch(intent)
        }
    }

    private fun runtimeDataSeeder(): MutableList<GenericModel> {
        val data: MutableList<GenericModel> = mutableListOf()
        data.add(GenericModel(1, "2022-01-01", 11, "la-di-da"))
        data.add(GenericModel(2, "2022-02-04", 11, "la-di-du"))
        data.add(GenericModel(3, "2022-03-07", 11, "la-de-de"))
        data.add(GenericModel(4, "2022-04-10", 11, "la-uk-uk"))
        data.add(GenericModel(5, "2022-05-13", 11, "la-xe-rs"))
        data.add(GenericModel(6, "2022-06-16", 11, "la-re-do"))
        data.add(GenericModel(7, "2022-07-19", 11, "ya-ku-za"))
        data.add(GenericModel(8, "2022-08-22", 11, "ni-er-me"))
        data.add(GenericModel(9, "2022-09-25", 11, "re-ro-ri"))
        data.add(GenericModel(10, "2022-10-28", 11, "xa-yb-zc"))
        return data
    }
}