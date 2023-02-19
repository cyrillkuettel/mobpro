package ch.hslu.mobpro.firstappfinal


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.hslu.mobpro.firstappfinal.lifecyclelog.LifecycleLogActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        val QUESTION = "question"
        val ANSWER = "answer"
    }

    private val openQuestionActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val textView = findViewById<TextView>(R.id.main_textView_result)
                var answer = resources.getString(R.string.main_text_gotAnswer) + "'"
                result.data?.let { data: Intent ->
                    data.extras?.let { extra: Bundle ->
                        answer = answer.plus(extra.getString(ANSWER) + "'")
                    }
                    textView.text = answer
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.main_button_logActivity).setOnClickListener { startLogActivity() }
        findViewById<Button>(R.id.main_button_startBrowser).setOnClickListener { startBrowser() }
        findViewById<Button>(R.id.main_button_questionActivity).setOnClickListener { startQuestionActivity() }
    }


    private fun startLogActivity() {
        val intent = Intent(this, LifecycleLogActivity::class.java)
        startActivity(intent)
    }

    private fun startBrowser() {
        val browserCall = Intent()
        browserCall.action = Intent.ACTION_VIEW
        browserCall.data = Uri.parse("http://www.hslu.ch")
        startActivity(browserCall)
    }

    private fun startQuestionActivity() {
        openQuestionActivity.launch(
         Intent(this, QuestionActivity::class.java).apply{
             putExtra(QUESTION, "und, wie läufts?")
         }
        )

    }
}