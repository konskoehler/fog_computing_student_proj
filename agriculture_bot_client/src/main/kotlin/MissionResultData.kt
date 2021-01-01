import kotlinx.serialization.Serializable

@Serializable
sealed class MissionResultData() {
    abstract val timestamp: Long
    abstract val plant: Plant
}

@Serializable
class InspectionResultData(override val timestamp: Long, override val plant: Plant, val pHLevel: Float, val soilMoisture: Float) : MissionResultData()

@Serializable
class WateringResultData(override val timestamp: Long, override val plant: Plant, val success: Boolean) : MissionResultData()
