package ge.gmikeladze.assignment3.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckItem(
    @ColumnInfo var description: String,
    @ColumnInfo val listId: Long,
    @ColumnInfo var isChecked: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}