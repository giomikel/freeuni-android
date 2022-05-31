package ge.gmikeladze.assignment3.mvvm.view_models

import androidx.lifecycle.ViewModel
import ge.gmikeladze.assignment3.data.entity.CheckItem
import ge.gmikeladze.assignment3.data.entity.ItemList
import ge.gmikeladze.assignment3.mvvm.repositories.CheckItemRepository
import ge.gmikeladze.assignment3.mvvm.repositories.ItemListRepository
import java.util.concurrent.LinkedBlockingQueue

class MainViewModel(
    private val itemListRepository: ItemListRepository,
    private val checkItemRepository: CheckItemRepository
) : ViewModel() {

    fun getItemLists(): List<ItemList> {
        val queue = LinkedBlockingQueue<List<ItemList>>()
        Thread {
            queue.add(itemListRepository.getItemLists())
        }.start()
        return queue.take()
    }

    fun getItemListsByKeyword(keyword: String): List<ItemList> {
        val queue = LinkedBlockingQueue<List<ItemList>>()
        Thread {
            queue.add(itemListRepository.getItemListsByKeyword(keyword))
        }.start()
        return queue.take()
    }

    fun getItemListCheckItems(listId: Int): List<CheckItem> {
        val queue = LinkedBlockingQueue<List<CheckItem>>()
        Thread {
            queue.add(checkItemRepository.getCheckItemsInItemList(listId))
        }.start()
        return queue.take()
    }

}