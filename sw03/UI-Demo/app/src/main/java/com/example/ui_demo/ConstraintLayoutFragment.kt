package com.example.ui_demo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel


class CounterViewModel : ViewModel() {

    var count = 0;

     fun incCounter() {
        count++
    }

    fun getCounter(): Int {
        return count
    }
}

class ConstraintLayoutFragment : Fragment(R.layout.fragment_constraint_layout) {
    private var TAG = "ConstraintLayoutFragment"
    private var counter = 0
    private val counterViewModel: CounterViewModel by activityViewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonFragmentcounter = view.findViewById<Button>(R.id.buttonFragmentCounter)
        val buttonIncViewModelCounter = view.findViewById<Button>(R.id.buttonIncViewModelCounter)

        buttonIncViewModelCounter.text = "Fragment $counter ++"
        counter = counterViewModel.getCounter()
        buttonFragmentcounter.text = "Fragment $counter ++"

        buttonFragmentcounter.setOnClickListener {fragmentcounterUp(buttonFragmentcounter)}

        buttonIncViewModelCounter.setOnClickListener { simpleCounterUp(buttonIncViewModelCounter)}
    }

    private fun fragmentcounterUp(button: Button) {
        Log.i(TAG, "fragmentcounterUp")
        counterViewModel.incCounter()
        counter = counterViewModel.getCounter()
        button.text = "Fragment $counter ++"
    }

    private fun simpleCounterUp(button: Button) {
        Log.i(TAG, "buttonIncViewCounterUp")
        counter++
        button.text = "Fragment $counter ++"
    }

}