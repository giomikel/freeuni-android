package ge.gmikeladze.assignment3.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ge.gmikeladze.assignment3.data.entity.CheckItem

@Dao
interface CheckItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCheckItem(item: CheckItem)

    @Query("SELECT * FROM CheckItem WHERE id = :itemId")
    fun getCheckItem(itemId: Int): CheckItem

    @Query("SELECT * FROM CheckItem WHERE listId = :listId")
    fun getListItems(listId: Int): List<CheckItem>

    @Query("DELETE FROM CheckItem WHERE id = :itemId")
    fun deleteCheckItem(itemId: Int)

    @Query("DELETE FROM CheckItem WHERE listId = :listId")
    fun deleteListItems(listId: Int)
}