package ge.gmikeladze.assignment3.mvvm.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.gmikeladze.assignment3.data.entity.CheckItem
import ge.gmikeladze.assignment3.databinding.CheckItemBinding

class CheckItemAdapter : RecyclerView.Adapter<CheckItemAdapter.CheckItemViewHolder>() {

    var checkItems: MutableList<CheckItem> = mutableListOf()
    var onCheckItemClickListener: CheckItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckItemViewHolder {
        return CheckItemViewHolder(
            CheckItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CheckItemViewHolder, position: Int) {
        if (checkItems.isNotEmpty())
            holder.bind(checkItems[position])
    }

    override fun getItemCount(): Int {
        return checkItems.size
    }

    inner class CheckItemViewHolder(private val binding: CheckItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(checkItem: CheckItem) {
            binding.checkItemName.setText(checkItem.description)
            binding.checkItemCheckbox.isChecked = checkItem.isChecked
            binding.checkItemName.isEnabled = !checkItem.isChecked
            binding.deleteButton.visibility =
                if (adapterPosition == checkItems.size - 1 && binding.checkItemName.isEnabled)
                    View.VISIBLE else View.GONE
            binding.checkItemCheckbox.setOnClickListener {
                onCheckItemClickListener?.onCheckboxClicked(checkItem, adapterPosition)
            }
            binding.deleteButton.setOnClickListener {
                onCheckItemClickListener?.onDeleteClicked(adapterPosition)
            }
//            binding.checkItemName.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
//                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
//                    checkItems[adapterPosition].description = binding.checkItemName.text.toString()
//                    return@OnKeyListener true
//                }
//                return@OnKeyListener false
//            })
            binding.checkItemName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    checkItems[adapterPosition].description = binding.checkItemName.text.toString()
                }
            })
            if (adapterPosition == checkItems.size - 1) {
                binding.checkItemName.requestFocus()
            }
        }
    }
}

interface CheckItemClickListener {
    fun onCheckboxClicked(checkItem: CheckItem, position: Int)

    fun onDeleteClicked(position: Int)
}