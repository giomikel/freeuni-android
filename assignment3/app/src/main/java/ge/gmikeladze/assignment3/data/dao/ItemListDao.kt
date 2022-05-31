package ge.gmikeladze.assignment3.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ge.gmikeladze.assignment3.data.entity.ItemList

@Dao
interface ItemListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemList(itemList: ItemList): Long

    @Query("SELECT * FROM ItemList ORDER BY lastUpdated DESC")
    fun getItemLists(): List<ItemList>

    @Query("SELECT * FROM ItemList WHERE id = :id")
    fun getItemListById(id: Int): ItemList

    @Query("SELECT * FROM ItemList WHERE name LIKE :keyword ORDER BY lastUpdated DESC")
    fun getItemListsByKeyword(keyword: String): List<ItemList>

    @Query("DELETE FROM ItemList WHERE id = :id")
    fun deleteItemList(id: Int)
}