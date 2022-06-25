package ge.gmikeladze.assignment4.model

data class AlarmItem(val time: String, var isSwitched: Boolean) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is AlarmItem -> {
                this.time == other.time
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return time.hashCode()
    }
}