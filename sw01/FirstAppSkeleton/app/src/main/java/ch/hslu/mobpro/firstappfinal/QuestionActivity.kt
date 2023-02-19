package ch.hslu.mobpro.firstappfinal

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class QuestionActivity : AppCompatActivity(R.layout.activity_questions) {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Read parameter and show on view.
		val question = intent.getStringExtra("question")
		question?.let { setQuestionText(it) }
		findViewById<View>(R.id.question_button_done).setOnClickListener { buttonClicked() }
	}

	private fun setQuestionText(question: String) {
		val textView = findViewById<View>(R.id.question_label) as TextView
		textView.text = question
	}

	private fun buttonClicked() {
		val answerData = Intent()

		answerData.putExtra("answer", answerText)
		setResult(RESULT_OK, answerData)
		finish()
	}

	private val answerText: String
		get() {
			val editText = findViewById<View>(R.id.question_text_answer) as EditText
			return editText.text.toString()
		}
}