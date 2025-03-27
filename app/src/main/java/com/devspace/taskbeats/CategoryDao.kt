package com.devspace.taskbeats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface CategoryDao {

    @Query("Select * From categoryentity")
    fun getAll():List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categoryEntity: List<CategoryEntity>)
}
