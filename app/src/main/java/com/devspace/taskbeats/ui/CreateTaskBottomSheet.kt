package com.devspace.taskbeats.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.devspace.taskbeats.R
import com.devspace.taskbeats.data.model.CategoryUiData
import com.devspace.taskbeats.data.model.TaskUiData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.Objects

class CreateTaskBottomSheet(
    private val categoryList: List<CategoryUiData>,
    private val onCreateClicked: (TaskUiData) -> Unit
) : BottomSheetDialogFragment() {

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_task_bottom_sheet, container, false)

        val btnCreate = view.findViewById<Button>(R.id.btn_task_create)
        val tieTaskName = view.findViewById<TextInputEditText>(R.id.tie_task)

        var taskCategory : String? = null

        btnCreate.setOnClickListener {
            val name = tieTaskName.text.toString()


            if(taskCategory != null) {

                onCreateClicked.invoke(
                    TaskUiData(
                        name = name,
                        category = requireNotNull(taskCategory)
                    )
                )

                dismiss()
            } else {
                Snackbar.make(btnCreate, "Select a category", Snackbar.LENGTH_SHORT).show()
            }

        }

        val categoryStr : List<String> = categoryList.map { it.name }

        val spinner: Spinner = view.findViewById(R.id.category_list)
        ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            categoryStr.toList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskCategory = categoryStr[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        return view
    }
}