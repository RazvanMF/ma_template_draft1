package ro.kensierrat.apptemplate.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
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
import ro.kensierrat.apptemplate.R
import ro.kensierrat.apptemplate.activities.ui.theme.Exam2025_try2Theme

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.infopage_layout)
        val localData = intent.getStringExtra("INFORMATION")
        val infoTextView: TextView = findViewById(R.id.infoText)
        infoTextView.text = localData

        prepareReturnButton()
    }

    fun prepareReturnButton() {
        val button: ImageButton = findViewById(R.id.infoPageReturn)
        button.setOnClickListener {
            val parentIntent: Intent = intent
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }
}
