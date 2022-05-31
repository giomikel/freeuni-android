package ge.gmikeladze.assignment3.mvvm.view_model_factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.gmikeladze.assignment3.data.TodoDatabase
import ge.gmikeladze.assignment3.mvvm.repositories.CheckItemRepository
import ge.gmikeladze.assignment3.mvvm.repositories.ItemListRepository
import ge.gmikeladze.assignment3.mvvm.view_models.EditViewModel

class EditViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            val db = TodoDatabase.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return EditViewModel(
                ItemListRepository(db.itemListDao()),
                CheckItemRepository(db.checkItemDao())
            ) as T
        }
        throw IllegalStateException("Invalid View Model")
    }

}