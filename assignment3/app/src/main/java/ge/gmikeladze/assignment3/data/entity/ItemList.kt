package ge.gmikeladze.assignment3.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemList(
    @ColumnInfo var name: String,
    @ColumnInfo var lastUpdated: Long,
    @ColumnInfo var isPinned: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}