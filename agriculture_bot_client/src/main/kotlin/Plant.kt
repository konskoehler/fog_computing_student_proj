import kotlinx.serialization.Serializable

@Serializable
data class Plant(val id: Long, val gpsPosition: GPSPosition)
