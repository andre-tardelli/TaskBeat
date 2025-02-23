package com.devspace.taskbeats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Query ("SELECT * FROM TaskEntity")
    fun getAll(): List<TaskEntity>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasksEntities: List<TaskEntity>)
}