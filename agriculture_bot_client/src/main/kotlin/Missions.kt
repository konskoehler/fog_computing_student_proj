import kotlinx.serialization.Serializable

@Serializable
open class Mission(val timestamp: Long, val plant: Plant)

class InspectionMission(timestamp: Long, plant: Plant) : Mission(timestamp, plant)

class WateringMission(timestamp: Long, plant: Plant, val quantity: Int) : Mission(timestamp, plant)