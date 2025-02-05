package ro.kensierrat.apptemplate.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import ro.kensierrat.apptemplate.R
import ro.kensierrat.apptemplate.domain.GenericModel
import java.io.ByteArrayOutputStream

class EditActivity : ComponentActivity() {
    var genericDate: String = ""
    var genericInt: Int = -1
    var genericString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editpage_layout)

        initializeData()
        prepareModifyButton()
        prepareReturnButton()
    }

    private fun initializeData() {
        val generic = intent.getParcelableExtra("OLD_MODEL", GenericModel::class.java)
        val dateField: EditText = findViewById(R.id.editpageDateText)
        val intField: EditText = findViewById(R.id.editpageIntText)
        val stringField: EditText = findViewById(R.id.editpageStringText)

        if (generic != null) {
            genericDate = generic.genericDate
            genericInt = generic.genericInt
            genericString = generic.genericString

            dateField.setText(genericDate)
            intField.setText(genericInt)
            stringField.setText(genericString)
        }
    }

    fun prepareModifyButton() {
        val button: Button = findViewById(R.id.editpageButton)
        val old_generic = intent.getParcelableExtra("OLD_MODEL", GenericModel::class.java)
        val position = intent.getParcelableExtra("POSITION", Integer::class.java)

        button.setOnClickListener {
            prepareData()
            if (!validateData()) {
                val builder = AlertDialog.Builder(this@EditActivity)
                builder.setMessage("VALIDATION FAILURE")
                    .setCancelable(false)
                    .setNeutralButton("OK", { dialog, id -> dialog.dismiss() })
                    .setTitle("PROMPT")

                val alert = builder.create()
                alert.show()
                return@setOnClickListener
            }

            val newAlbum = GenericModel(old_generic!!.id, genericDate, genericInt, genericString)

            val parentIntent: Intent = intent
            parentIntent.putExtra("COMPONENT_RETURNED", newAlbum)
            parentIntent.putExtra("POSITION", position)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun prepareReturnButton() {
        val button: ImageButton = findViewById(R.id.editPageReturn)
        button.setOnClickListener {
            val parentIntent: Intent = intent
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun prepareData() {
        val dateField: EditText = findViewById(R.id.editpageDateText)
        val intField: EditText = findViewById(R.id.editpageIntText)
        val stringField: EditText = findViewById(R.id.editpageStringText)

        genericDate = dateField.text.toString()
        genericInt = intField.text.toString().toInt()
        genericString = stringField.text.toString()
    }

    private fun validateData() : Boolean {
        if (genericDate.isEmpty() || genericString.isEmpty())
            return false;
        return true;
    }
}