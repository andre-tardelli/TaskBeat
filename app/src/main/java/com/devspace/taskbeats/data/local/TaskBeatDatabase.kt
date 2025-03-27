package com.devspace.taskbeats.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devspace.taskbeats.data.local.CategoryEntity
import com.devspace.taskbeats.data.local.TaskEntity
import com.devspace.taskbeats.data.local.CategoryDao
import com.devspace.taskbeats.data.local.TaskDao

@Database([CategoryEntity::class, TaskEntity::class], version =  5)
abstract class TaskBeatDatabase : RoomDatabase(){

    abstract fun getCategoryDao(): CategoryDao

    abstract fun getTaskDao(): TaskDao
}