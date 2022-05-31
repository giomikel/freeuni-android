package ge.gmikeladze.assignment3.mvvm.view_models

import android.content.ClipData
import androidx.lifecycle.ViewModel
import ge.gmikeladze.assignment3.data.entity.CheckItem
import ge.gmikeladze.assignment3.data.entity.ItemList
import ge.gmikeladze.assignment3.mvvm.repositories.CheckItemRepository
import ge.gmikeladze.assignment3.mvvm.repositories.ItemListRepository
import java.util.concurrent.LinkedBlockingQueue

class EditViewModel(
    private val itemListRepository: ItemListRepository,
    private val checkItemRepository: CheckItemRepository
) : ViewModel() {

    fun addCheckItem(item: CheckItem) {
        Thread {
            checkItemRepository.addCheckItem(item)
        }.start()
    }

    fun addItemList(itemList: ItemList): Long {
        val queue = LinkedBlockingQueue<Long>()
        Thread {
            queue.add(itemListRepository.addItemList(itemList))
        }.start()
        return queue.take()
    }

    fun getItemListById(id: Int): ItemList {
        val queue = LinkedBlockingQueue<ItemList>()
        Thread {
            queue.add(itemListRepository.getItemListById(id))
        }.start()
        return queue.take()
    }

    fun getCheckItemsList(listId: Int): List<CheckItem> {
        val queue = LinkedBlockingQueue<List<CheckItem>>()
        Thread {
            queue.add(checkItemRepository.getCheckItemsInItemList(listId))
        }.start()
        return queue.take()
    }

    fun deleteCheckItem(id: Int) {
        Thread {
            checkItemRepository.deleteCheckItem(id)
        }.start()
    }

    fun deleteItemList(listId: Int) {
        Thread {
            itemListRepository.deleteItemList(listId)
        }.start()
    }

    fun deleteListItems(listId: Int) {
        Thread {
            checkItemRepository.deleteCheckItemsInList(listId)
        }.start()
    }
}