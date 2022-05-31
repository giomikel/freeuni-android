package ge.gmikeladze.assignment3.mvvm.repositories

import ge.gmikeladze.assignment3.data.dao.ItemListDao
import ge.gmikeladze.assignment3.data.entity.ItemList

class ItemListRepository(private val itemListDao: ItemListDao) {

    fun addItemList(itemList: ItemList): Long = itemListDao.addItemList(itemList)

    fun getItemLists() = itemListDao.getItemLists()

    fun getItemListById(id: Int) = itemListDao.getItemListById(id)

    fun getItemListsByKeyword(keyword: String) = itemListDao.getItemListsByKeyword(keyword)

    fun deleteItemList(id: Int) = itemListDao.deleteItemList(id)
}