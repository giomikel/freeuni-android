package ge.gmikeladze.assignment3.mvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ge.gmikeladze.assignment3.R
import ge.gmikeladze.assignment3.data.entity.CheckItem
import ge.gmikeladze.assignment3.data.entity.ItemList
import ge.gmikeladze.assignment3.databinding.ItemListItemBinding

class ItemListAdapter : RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    var itemListList: MutableList<ItemList> = mutableListOf()
    var itemListsCheckItems: MutableList<List<CheckItem>> = mutableListOf()
    var onItemListClickListener: ItemListClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        return ItemListViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        if (itemListList.isNotEmpty() && itemListsCheckItems.isNotEmpty())
            holder.bind(itemListList[position], itemListsCheckItems[position])
    }

    override fun getItemCount(): Int {
        return itemListList.size
    }

    inner class ItemListViewHolder(private val binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemList: ItemList, checkItems: List<CheckItem>) {
            binding.itemListName.text = itemList.name
            binding.checkItem1.visibility = View.GONE
            binding.checkItem2.visibility = View.GONE
            binding.checkItem3.visibility = View.GONE
            binding.threeDots.visibility = View.GONE
            binding.checkedItemsText.visibility = View.GONE
            val checkboxes = arrayOf(binding.checkItem1, binding.checkItem2, binding.checkItem3)
            var uncheckedCount = 0
            var index = 0
            checkItems.forEach { checkItem ->
                if (!checkItem.isChecked) {
                    uncheckedCount += 1
                    if (index < 3) {
                        checkboxes[index].visibility = View.VISIBLE
                        checkboxes[index].isChecked = checkItem.isChecked
                        checkboxes[index].text = checkItem.description
                        index += 1
                    }
                }
            }
            if (checkItems.size > 3) {
                binding.threeDots.visibility = View.VISIBLE
            }
            if (uncheckedCount < checkItems.size) {
                binding.checkedItemsText.visibility = View.VISIBLE
                val checkedText = "+ ${checkItems.size - uncheckedCount} checked items"
                binding.checkedItemsText.text = checkedText
            }
            binding.root.setOnClickListener {
                onItemListClickListener?.onItemListClicked(itemList)
            }
            binding.root.background =
                ContextCompat.getDrawable(binding.root.context, R.drawable.border_frame)
        }
    }
}

interface ItemListClickListener {
    fun onItemListClicked(itemList: ItemList)
}