package ro.kensierrat.apptemplate.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import ro.kensierrat.apptemplate.R
import ro.kensierrat.apptemplate.domain.GenericModel
import java.io.ByteArrayOutputStream
import java.util.Random
import java.util.UUID

class AddActivity : ComponentActivity() {
    var date: String = ""
    var int: Int = 0
    var string: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addpage_layout)

        prepareAddButton()
        prepareReturnButton()
    }

    fun prepareAddButton() {
        val button: Button = findViewById(R.id.addpageButton)

        button.setOnClickListener {
            prepareData()
            if (!validateData()) {
                val builder = AlertDialog.Builder(this@AddActivity)
                builder.setMessage("VALIDATION FAILURE")
                    .setCancelable(false)
                    .setNeutralButton("OK", { dialog, id -> dialog.dismiss() })
                    .setTitle("PROMPT")

                val alert = builder.create()
                alert.show()
                return@setOnClickListener
            }

            val newAlbum = GenericModel(-1, date, int, string)

            val parentIntent: Intent = intent
            parentIntent.putExtra("COMPONENT_RETURNED", newAlbum)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun prepareReturnButton() {
        val button: ImageButton = findViewById(R.id.addPageReturn)
        button.setOnClickListener {
            val parentIntent: Intent = intent
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun prepareData() {
        val dateField: EditText = findViewById(R.id.addpageDateText)
        val intField: EditText = findViewById(R.id.addpageIntText)
        val stringField: EditText = findViewById(R.id.addpageStringText)

        date = dateField.text.toString()
        int = intField.text.toString().toInt()
        string = stringField.text.toString()
    }

    private fun validateData() : Boolean {
        if (date.isEmpty() || string.isEmpty())
            return false;
        return true;
    }
}