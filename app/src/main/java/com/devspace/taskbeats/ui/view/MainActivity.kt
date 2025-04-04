package com.devspace.taskbeats.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.devspace.taskbeats.R
import com.devspace.taskbeats.data.local.CategoryEntity
import com.devspace.taskbeats.data.local.TaskBeatDatabase
import com.devspace.taskbeats.data.local.TaskEntity
import com.devspace.taskbeats.data.model.CategoryUiData
import com.devspace.taskbeats.data.model.TaskUiData
import com.devspace.taskbeats.ui.CreateCategoryBottomSheet
import com.devspace.taskbeats.ui.CreateTaskBottomSheet
import com.devspace.taskbeats.ui.adapter.CategoryListAdapter
import com.devspace.taskbeats.ui.adapter.TaskListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var categories = listOf<CategoryUiData>()
    private var tasks = listOf<TaskUiData>()
    private val categoryAdapter = CategoryListAdapter()

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskBeatDatabase::class.java, "database-task-beat"
        ).fallbackToDestructiveMigration()
         .build()
    }

    private val categoryDao by lazy {
        db.getCategoryDao()
    }

    private val taskDao by lazy {
        db.getTaskDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        val rvTask = findViewById<RecyclerView>(R.id.rv_tasks)
        val fabCreateTask = findViewById<FloatingActionButton>(R.id.fab_creat_task)

        fabCreateTask.setOnClickListener {
            val createTaskBottomSheet = CreateTaskBottomSheet(
                categories
            ) { taskToBeCreated ->

            }
            createTaskBottomSheet.show(
                supportFragmentManager,
                "createTaskBottomShee"
            )
        }

        val taskAdapter = TaskListAdapter()


        categoryAdapter.setOnClickListener { selected ->
            if(selected.name == "+") {
                val createCategoryBottomSheet = CreateCategoryBottomSheet { categoryName ->
                    val categoryEntity = CategoryEntity(
                        name = categoryName,
                        isSelected = false
                    )

                    insertCategory(categoryEntity)
                }

                createCategoryBottomSheet.show(supportFragmentManager, "createCategoryBottomSheet")
            } else {
                val categoryTemp = categories.map { item ->
                    when {
                        item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                        item.name == selected.name && item.isSelected -> item.copy(isSelected = false)
                        else -> item
                    }
                }

                val taskTemp =
                    if (selected.name != "ALL") {
                        tasks.filter { it.category == selected.name }
                    } else {
                        tasks
                    }
                taskAdapter.submitList(taskTemp)

                categoryAdapter.submitList(categoryTemp)
            }
        }

        rvCategory.adapter = categoryAdapter
        GlobalScope.launch(Dispatchers.IO) {
            getCategoriesFromDataBase()
        }


        rvTask.adapter = taskAdapter
        getTasksFromDataBase(taskAdapter)
    }

    private fun getCategoriesFromDataBase() {
        val categoriesFromDb: List<CategoryEntity> = categoryDao.getAll()
        val categoriesUiData = categoriesFromDb.map {
            CategoryUiData(
                name = it.name,
                isSelected = it.isSelected
            )
        }

            .toMutableList()

        categoriesUiData.add(
            CategoryUiData(
                name = "+",
                isSelected = false,
            )
        )
        GlobalScope.launch(Dispatchers.Main) {
            categories = categoriesUiData
            categoryAdapter.submitList(categoriesUiData)
        }
    }
    private fun getTasksFromDataBase(adapter: TaskListAdapter) {
        GlobalScope.launch (Dispatchers.IO) {
            val tasksFromDb: List<TaskEntity> = taskDao.getAll()
            val tasksUiData = tasksFromDb.map {
                TaskUiData(
                    name = it.name,
                    category = it.category
                )
            }
            GlobalScope.launch(Dispatchers.Main) {
                tasks = tasksUiData
                adapter.submitList(tasksUiData)
            }
        }
    }

    private fun insertCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch (Dispatchers.IO) {
            categoryDao.inset(categoryEntity)
            getCategoriesFromDataBase()
        }

    }
}
