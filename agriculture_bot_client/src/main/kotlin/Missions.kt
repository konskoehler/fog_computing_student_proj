@Serializable
sealed class Mission() {
}

@Serializable
data class InspectionMission(val gpsPosition: GPSPosition) : Mission()

@Serializable
data class WateringMission(val gpsPosition: GPSPosition, val quantity: Int) : Mission()