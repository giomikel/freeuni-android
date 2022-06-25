package ge.gmikeladze.assignment4.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.gmikeladze.assignment4.databinding.AlarmItemBinding
import ge.gmikeladze.assignment4.model.AlarmItem

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    var alarmItems: MutableList<AlarmItem> = mutableListOf()
    var onItemClickListener: AlarmItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            AlarmItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(alarmItems[position])
        holder.alarmItem.setOnLongClickListener {
            onItemClickListener!!.onItemLongClicked(alarmItems[position])
            return@setOnLongClickListener true
        }
        holder.alarmSwitch.setOnCheckedChangeListener { _, isSwitched ->
            onItemClickListener!!.onSwitchClicked(alarmItems[position], isSwitched)
        }
    }

    override fun getItemCount(): Int {
        return alarmItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: MutableList<AlarmItem>) {
        alarmItems = items
        notifyDataSetChanged()
    }

    inner class AlarmViewHolder(private val binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val alarmItem = binding.alarmItem
        val alarmSwitch = binding.alarmSwitch

        fun bind(alarmItem: AlarmItem) {
            binding.alarmTime.text = alarmItem.time
            binding.alarmSwitch.isChecked = alarmItem.isSwitched
        }
    }
}

interface AlarmItemClickListener {
    fun onItemLongClicked(alarmItem: AlarmItem)
    fun onSwitchClicked(alarmItem: AlarmItem, isSwitched: Boolean)
}