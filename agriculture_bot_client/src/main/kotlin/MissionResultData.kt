import kotlinx.serialization.Serializable

@Serializable
sealed class MissionResultData(val timestamp: Long, val plant: Plant)

class InspectionResultData(timestamp: Long, plant: Plant, val pHLevel: Float, val soilMoisture: Float) : MissionResultData(timestamp, plant)

class WateringResultData(timestamp: Long, plant: Plant, val success: Boolean) : MissionResultData(timestamp, plant)
