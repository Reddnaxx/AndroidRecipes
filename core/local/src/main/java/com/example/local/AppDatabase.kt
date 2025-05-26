package com.example.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.local.dao.RecipeDao
import com.example.profile.domain.entities.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java, "recipes-db"
            ).fallbackToDestructiveMigration(true).build()
    }
}
