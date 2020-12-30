import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
sealed class MissionResultData() {
    abstract val timestamp: Long
    abstract val plant: Plant
    abstract val requestedMission: Mission
}

@Serializable
class InspectionResultData(
    override val timestamp: Long,
    override val plant: Plant,
    override val requestedMission: Mission,
    val pHLevel: Float,
    val soilMoisture: Float
) : MissionResultData()

@Serializable
class WateringResultData(
    override val timestamp: Long,
    override val plant: Plant,
    override val requestedMission: Mission,
    val success: Boolean
) : MissionResultData()
