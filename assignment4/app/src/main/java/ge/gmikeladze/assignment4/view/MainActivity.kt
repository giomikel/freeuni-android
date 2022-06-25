package ge.gmikeladze.assignment4.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.gmikeladze.assignment4.AlarmBroadcast
import ge.gmikeladze.assignment4.R
import ge.gmikeladze.assignment4.view_model.MainViewModel
import ge.gmikeladze.assignment4.view_model.MainViewModel.Companion.ALARM_ITEMS_NAME
import ge.gmikeladze.assignment4.view_model_factory.MainViewModelFactory
import ge.gmikeladze.assignment4.adapter.AlarmAdapter
import ge.gmikeladze.assignment4.adapter.AlarmItemClickListener
import ge.gmikeladze.assignment4.databinding.ActivityMainBinding
import ge.gmikeladze.assignment4.model.AlarmItem
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmRV: RecyclerView
    private lateinit var adapter: AlarmAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(applicationContext)
        ).get(MainViewModel::class.java)
    }

    private val alarmItemClickListener = object : AlarmItemClickListener {
        override fun onItemLongClicked(alarmItem: AlarmItem) {
            AlertDialog
                .Builder(this@MainActivity)
                .setTitle(resources.getString(R.string.delete_item))
                .setPositiveButton(resources.getString(R.string.positive)) { dialogInterface, _ ->
                    adapter.alarmItems.remove(alarmItem)
                    adapter.setItems(adapter.alarmItems)
                    val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                    alarmManager.cancel(getPendingIntent(alarmItem))
                    viewModel.saveToSharedPreferences(adapter.alarmItems)
                    dialogInterface.dismiss()
                }
                .setNegativeButton(resources.getString(R.string.negative)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .create()
                .show()
        }

        override fun onSwitchClicked(alarmItem: AlarmItem, isSwitched: Boolean) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val pendingIntent = getPendingIntent(alarmItem)
            if (isSwitched) {
                alarmItem.isSwitched = true
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, alarmItem.time.substringBefore(":").toInt())
                calendar.set(Calendar.MINUTE, alarmItem.time.substringAfter(":").toInt())
                calendar.set(Calendar.SECOND, 0)
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmItem.isSwitched = false
                alarmManager.cancel(pendingIntent)
            }
            viewModel.saveToSharedPreferences(adapter.alarmItems)
        }

    }

    private val timePickListener = TimePickerDialog.OnTimeSetListener { _, p1, p2 ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, p1)
        calendar.set(Calendar.MINUTE, p2)
        calendar.set(Calendar.SECOND, 0)
        addAlarm(calendar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.model = viewModel
        binding.lifecycleOwner = this
        binding.switchTextView.setOnClickListener {
            viewModel.changeSwitchText()
        }
        rvSetup()
        initAlarmItems()
        binding.addButton.setOnClickListener {
            pickTime()
        }
    }

    private fun rvSetup() {
        alarmRV = binding.alarmRV
        alarmRV.layoutManager = LinearLayoutManager(this)
        adapter = AlarmAdapter()
        adapter.onItemClickListener = alarmItemClickListener
        alarmRV.adapter = adapter
    }

    private fun initAlarmItems() {
        val alarmList = applicationContext.getSharedPreferences(
            MainViewModel.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        ).getStringSet(ALARM_ITEMS_NAME, mutableSetOf())
        if (alarmList != null && alarmList.isNotEmpty()) {
            val temp = mutableListOf<AlarmItem>()
            for (item in alarmList) {
                val isChecked = item.last() == 'T'
                temp.add(AlarmItem(item.substring(0, item.length - 1), isChecked))
            }
            updateAlarmItems(temp)
        }
    }

    private fun updateAlarmItems(items: MutableList<AlarmItem>) {
        items.sortWith(compareBy({ it.time }, { it.isSwitched }))
        adapter.setItems(items)
    }

    private fun pickTime() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this, timePickListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun addAlarm(calendar: Calendar) {
        if (calendar.timeInMillis > System.currentTimeMillis()) {
            val time = SimpleDateFormat("HH:mm").format(calendar.time)
            val alarmItems = adapter.alarmItems
            val alarmItem = AlarmItem(time, true)
            if (!alarmItems.contains(alarmItem)) {
                alarmItems.add(alarmItem)
                updateAlarmItems(alarmItems)
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    getPendingIntent(alarmItem)
                )
                viewModel.saveToSharedPreferences(adapter.alarmItems)
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(alarmItem: AlarmItem): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            alarmItem.time.substringAfter(":").toInt()
                    + alarmItem.time.substringBefore(":").toInt() * 60,
            Intent(AlarmBroadcast.ALARM_ACTION_NAME).apply {
                `package` = packageName
                putExtra(INTENT_TIME_EXTRA, alarmItem.time)
            },
            0
        )
    }

    companion object {
        const val INTENT_TIME_EXTRA = "alarmTime"
    }
}