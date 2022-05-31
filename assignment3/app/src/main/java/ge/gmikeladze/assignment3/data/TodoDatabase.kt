package ge.gmikeladze.assignment3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ge.gmikeladze.assignment3.data.dao.CheckItemDao
import ge.gmikeladze.assignment3.data.dao.ItemListDao
import ge.gmikeladze.assignment3.data.entity.CheckItem
import ge.gmikeladze.assignment3.data.entity.ItemList

@Database(entities = [CheckItem::class, ItemList::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun checkItemDao(): CheckItemDao
    abstract fun itemListDao(): ItemListDao

    companion object {
        private const val DATABASE_NAME = "todo-db"

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(context, TodoDatabase::class.java, DATABASE_NAME)
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}