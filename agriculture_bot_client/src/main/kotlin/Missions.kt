import kotlinx.serialization.Serializable

@Serializable
sealed class Mission

data class InspectionMission(val timestamp: Long, val gpsPosition: GPSPosition) : Mission()

data class WateringMission(val timestamp: Long, val gpsPosition: GPSPosition, val quantity: Int) : Mission()