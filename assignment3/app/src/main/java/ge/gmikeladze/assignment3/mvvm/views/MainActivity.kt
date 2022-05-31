package ge.gmikeladze.assignment3.mvvm.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ge.gmikeladze.assignment3.data.entity.ItemList
import ge.gmikeladze.assignment3.databinding.ActivityMainBinding
import ge.gmikeladze.assignment3.mvvm.adapters.ItemListAdapter
import ge.gmikeladze.assignment3.mvvm.adapters.ItemListClickListener
import ge.gmikeladze.assignment3.mvvm.view_model_factories.MainViewModelFactory
import ge.gmikeladze.assignment3.mvvm.view_models.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchBar: EditText
    private lateinit var pinnedRV: RecyclerView
    private lateinit var pinnedRVAdapter: ItemListAdapter
    private lateinit var unpinnedRV: RecyclerView
    private lateinit var unpinnedRVAdapter: ItemListAdapter
    private lateinit var addButton: ImageButton

    private val itemListClickListener = object : ItemListClickListener {
        override fun onItemListClicked(itemList: ItemList) {
            onItemListClickedInMain(itemList)
        }
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(applicationContext)
        ).get(MainViewModel::class.java)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                fetchItemListsWithCheckItems()
                Log.d("return", "back to main")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.model = viewModel
        binding.lifecycleOwner = this

        searchBar = binding.searchBar
        pinnedRVInit()
        unpinnedRVInit()

        searchBar.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                fetchItemListsWithCheckItems(searchBar.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        addButton = binding.addItemButton
        addButton.setOnClickListener {
            launchEditActivity(ADD_ITEM_IDENTIFIER, ADD_ITEM_NAME)
        }
    }


    override fun onResume() {
        super.onResume()
        fetchItemListsWithCheckItems(null)
    }

    private fun pinnedRVInit() {
        pinnedRV = binding.pinnedRV
        pinnedRV.layoutManager =
            StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        pinnedRVAdapter = ItemListAdapter()
        pinnedRV.adapter = pinnedRVAdapter
        pinnedRVAdapter.onItemListClickListener = itemListClickListener
    }

    private fun unpinnedRVInit() {
        unpinnedRV = binding.unpinnedRV
        unpinnedRV.layoutManager =
            StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        unpinnedRVAdapter = ItemListAdapter()
        unpinnedRV.adapter = unpinnedRVAdapter
        unpinnedRVAdapter.onItemListClickListener = itemListClickListener
    }

    private fun onItemListClickedInMain(itemList: ItemList) {
        launchEditActivity(itemList.id, itemList.name)
    }

    private fun launchEditActivity(identifier: Int, name: String) {
        val intent = Intent(this, EditActivity::class.java).apply {
            putExtra(EditActivity.INTENT_EXTRA_IDENTIFIER, identifier)
            putExtra(EditActivity.INTENT_EXTRA_NAME, name)
        }
        resultLauncher.launch(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchItemListsWithCheckItems(keyword: String?) {
        binding.pinnedText.visibility = View.GONE
        binding.otherText.visibility = View.GONE
        pinnedRVInit()
        unpinnedRVInit()
        val listOfItemLists =
            if (keyword == null || keyword == "") viewModel.getItemLists()
            else viewModel.getItemListsByKeyword("%${keyword}%")
        listOfItemLists.forEach { itemList ->
            if (itemList.isPinned) {
                binding.pinnedText.visibility = View.VISIBLE
                binding.otherText.visibility = View.VISIBLE
                pinnedRVAdapter.itemListList.add(itemList)
                val checkItems = viewModel.getItemListCheckItems(itemList.id)
                pinnedRVAdapter.itemListsCheckItems.add(checkItems)
            } else {
                unpinnedRVAdapter.itemListList.add(itemList)
                val checkItems = viewModel.getItemListCheckItems(itemList.id)
                unpinnedRVAdapter.itemListsCheckItems.add(checkItems)
            }
        }
        pinnedRVAdapter.notifyDataSetChanged()
        unpinnedRVAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val COLUMN_COUNT = 2
        const val ADD_ITEM_IDENTIFIER = -1
        const val ADD_ITEM_NAME = ""
    }
}