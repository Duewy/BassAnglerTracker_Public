package com.bramestorm.bassanglertracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class PopupLengthEntryMetric : Activity() {

    private var selectedSpecies: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_length_entry_metric)

        val edtLengthCm: EditText = findViewById(R.id.edtLengthCms)
        val edtLengthDecimal: EditText = findViewById(R.id.edtLengthDecimal)
        val btnSaveCatch: Button = findViewById(R.id.btnSaveCatch)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val spinnerSpecies: Spinner = findViewById(R.id.spinnerCmsSpeciesPopUp)

        // Load species list from strings.xml
        val speciesArray = resources.getStringArray(R.array.species_list)

        // Set up the spinner with species list
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, speciesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSpecies.adapter = adapter

// Update selectedSpecies when user picks an item
        spinnerSpecies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedSpecies = speciesArray[position] // Update selectedSpecies
                Log.d("DB_DEBUG", "Species selected: $selectedSpecies") // Debugging log
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSpecies = "" // Reset if nothing is selected
            }
        }

        // `````````````` Apply InputFilters to limit values  ````````````````````
        edtLengthCm.filters = arrayOf(MinMaxInputFilter(0, 99)) // Cms: 1-99
        edtLengthDecimal.filters = arrayOf(MinMaxInputFilter(0, 9)) // millimeters 0-9

        // ````````````````````` Save button functionality ````````````````````
        btnSaveCatch.setOnClickListener {
            val resultIntent = Intent()

            val lengthCm = edtLengthCm.text.toString().toIntOrNull() ?: 0
            val lengthDecimal = edtLengthDecimal.text.toString().toIntOrNull() ?: 0
            val totalLengthCms = ((lengthCm * 10) + lengthDecimal)

            resultIntent.putExtra("lengthTotalCms", totalLengthCms)
            resultIntent.putExtra("selectedSpecies", selectedSpecies)

            Log.d("DB_DEBUG", "🚀 Returning weight from Pop Up: $totalLengthCms oz, Species: $selectedSpecies")

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // ~~~~~~~~~~~~~~~~ Cancel button functionality  ~~~~~~~~~~~~~~~~~~~~~
        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    // -------------- Min & Max Input Filter to enforce value limits --------------------------
    class MinMaxInputFilter(private val min: Int, private val max: Int) : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = (dest.toString() + source.toString()).toInt()
                if (input in min..max) {
                    return null // Accept input
                }
            } catch (e: NumberFormatException) {
                // Ignore invalid input
            }
            return "" // Reject input if out of range
        }
    }
}
