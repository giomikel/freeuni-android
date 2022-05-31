package ge.gmikeladze.assignment3.mvvm.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.gmikeladze.assignment3.R
import ge.gmikeladze.assignment3.data.entity.CheckItem
import ge.gmikeladze.assignment3.data.entity.ItemList
import ge.gmikeladze.assignment3.databinding.ActivityEditBinding
import ge.gmikeladze.assignment3.mvvm.adapters.CheckItemAdapter
import ge.gmikeladze.assignment3.mvvm.adapters.CheckItemClickListener
import ge.gmikeladze.assignment3.mvvm.view_model_factories.EditViewModelFactory
import ge.gmikeladze.assignment3.mvvm.view_models.EditViewModel
import java.util.*

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var uncheckedRV: RecyclerView
    private lateinit var uncheckedRVAdapter: CheckItemAdapter
    private lateinit var checkedRV: RecyclerView
    private lateinit var checkedRVAdapter: CheckItemAdapter
    private var listId: Long = -1
    private var isPinned = false

    private val checkItemClickListener = object : CheckItemClickListener {
        override fun onCheckboxClicked(checkItem: CheckItem, position: Int) {
            handleCheckBoxClick(checkItem, position)
        }

        override fun onDeleteClicked(position: Int) {
            handleDeleteClick(position)
        }
    }

    private val viewModel: EditViewModel by lazy {
        ViewModelProvider(
            this,
            EditViewModelFactory(applicationContext)
        ).get(EditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.model = viewModel
        binding.lifecycleOwner = this

        uncheckedRVInit()
        checkedRVInit()
        itemsInit()
        binding.pinIcon.setOnClickListener {
            isPinned = !isPinned
            redrawPinIcon()
        }
        binding.addListItemButton.setOnClickListener {
            addCheckItem()
        }
        binding.backIcon.setOnClickListener {
            handleBackButton()
        }
    }

    override fun onBackPressed() {
        handleBackButton()
    }

    private fun uncheckedRVInit() {
        uncheckedRV = binding.uncheckedRV
        uncheckedRV.layoutManager = LinearLayoutManager(this)
        uncheckedRVAdapter = CheckItemAdapter()
        uncheckedRVAdapter.onCheckItemClickListener = checkItemClickListener
        uncheckedRV.adapter = uncheckedRVAdapter
    }

    private fun checkedRVInit() {
        checkedRV = binding.checkedRV
        checkedRV.layoutManager = LinearLayoutManager(this)
        checkedRVAdapter = CheckItemAdapter()
        checkedRVAdapter.onCheckItemClickListener = checkItemClickListener
        checkedRV.adapter = checkedRVAdapter
    }

    private fun itemsInit() {
        val identifier = intent.getIntExtra(INTENT_EXTRA_IDENTIFIER, -2)
        val name = intent.getStringExtra(INTENT_EXTRA_NAME)
        listId = identifier.toLong()
        binding.divider.visibility = View.GONE
        if (identifier != MainActivity.ADD_ITEM_IDENTIFIER) {
            fetchItemsWithId(identifier)
            val itemList = viewModel.getItemListById(identifier)
            isPinned = itemList.isPinned
            redrawPinIcon()
        }
        binding.itemListName.setText(name)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchItemsWithId(identifier: Int) {
        val checkItems = viewModel.getCheckItemsList(identifier)
        checkItems.map { checkItem ->
            if (checkItem.isChecked) {
                binding.divider.visibility = View.VISIBLE
                checkedRVAdapter.checkItems.add(checkItem)
            } else {
                uncheckedRVAdapter.checkItems.add(checkItem)
            }
        }
        checkedRVAdapter.notifyDataSetChanged()
        uncheckedRVAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleCheckBoxClick(checkItem: CheckItem, position: Int) {
        if (checkItem.isChecked) {
            checkedRVAdapter.checkItems.removeAt(position)
            checkItem.isChecked = false
            uncheckedRVAdapter.checkItems.add(checkItem)
            if (checkedRVAdapter.checkItems.isEmpty())
                binding.divider.visibility = View.GONE
        } else {
            uncheckedRVAdapter.checkItems.removeAt(position)
            checkItem.isChecked = true
            checkedRVAdapter.checkItems.add(checkItem)
            binding.divider.visibility = View.VISIBLE
        }
        checkedRVAdapter.notifyDataSetChanged()
        uncheckedRVAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleDeleteClick(position: Int) {
        viewModel.deleteCheckItem(uncheckedRVAdapter.checkItems[position].id)
        uncheckedRVAdapter.checkItems.removeAt(position)
        uncheckedRVAdapter.notifyDataSetChanged()
    }

    private fun redrawPinIcon() {
        binding.pinIcon.background = if (isPinned) ContextCompat.getDrawable(
            this, R.drawable.ic_pinned
        ) else ContextCompat.getDrawable(this, R.drawable.ic_pin)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addCheckItem() {
        uncheckedRVAdapter.checkItems.add(CheckItem("", listId, false))
        uncheckedRVAdapter.notifyDataSetChanged()
    }

    private fun handleBackButton() {
        if (binding.itemListName.text.isEmpty()) {
            binding.itemListName.setText(EMPTY_LIST_NAME)
        }
        val itemList = ItemList(
            binding.itemListName.text.toString(),
            Calendar.getInstance().timeInMillis,
            isPinned
        )
        itemList.id = if (listId.toInt() == -1) 0 else listId.toInt()
        val checkItems = checkedRVAdapter.checkItems + uncheckedRVAdapter.checkItems
        if (checkItems.isNotEmpty()) {
            listId = viewModel.addItemList(itemList)
            for (item in checkItems) {
                if (item.description.isNotEmpty()) {
                    val itm = CheckItem(item.description, listId, item.isChecked)
                    itm.id = item.id
                    viewModel.addCheckItem(itm)
                } else {
                    viewModel.deleteCheckItem(item.id)
                }
            }
        } else {
            viewModel.deleteItemList(listId.toInt())
            viewModel.deleteListItems(listId.toInt())
        }
        finish()
    }

    companion object {
        const val INTENT_EXTRA_IDENTIFIER: String = "IDENTIFIER"
        const val INTENT_EXTRA_NAME: String = "NAME"
        const val EMPTY_LIST_NAME: String = "UNTITLED"
    }
}