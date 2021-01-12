import kotlinx.serialization.Serializable

@Serializable
sealed class MissionResultData() {
    abstract val timestamp: Long
}

@Serializable
class InspectionResultData(
    override val timestamp: Long,
    val pHLevel: Float,
    val soilMoisture: Float
) : MissionResultData()

@Serializable
class WateringResultData(
    override val timestamp: Long,
    val success: Boolean
) : MissionResultData()
