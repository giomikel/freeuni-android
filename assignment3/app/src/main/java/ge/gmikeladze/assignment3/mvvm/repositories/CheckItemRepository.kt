package ge.gmikeladze.assignment3.mvvm.repositories

import ge.gmikeladze.assignment3.data.dao.CheckItemDao
import ge.gmikeladze.assignment3.data.entity.CheckItem

class CheckItemRepository(private val checkItemDao: CheckItemDao) {

    fun addCheckItem(item: CheckItem) = checkItemDao.addCheckItem(item)

    fun getCheckItem(id: Int) = checkItemDao.getCheckItem(id)

    fun getCheckItemsInItemList(listId: Int) = checkItemDao.getListItems(listId)

    fun deleteCheckItem(itemId: Int) = checkItemDao.deleteCheckItem(itemId)

    fun deleteCheckItemsInList(listId: Int) = checkItemDao.deleteListItems(listId)
}