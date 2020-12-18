@Serializable
sealed class Mission() {
}

@Serializable
data class InspectionMission(val timestamp: Long, val gpsPosition: GPSPosition) : Mission()

@Serializable
data class WateringMission(val timestamp: Long, val gpsPosition: GPSPosition, val quantity: Int) : Mission()