import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
sealed class Mission() {
    abstract val timestamp: Long
    abstract val plant: Plant
    abstract val _id: Int
    abstract val resultData: MissionResultData?
    abstract val timestampSentToClient: Int?
}

@Serializable
data class InspectionMission(
    override val timestamp: Long,
    override val plant: Plant,
    override val _id: Int = ObjectId().hashCode(),
    override val resultData: MissionResultData? = null,
    override val timestampSentToClient: Int? = null,
) :
    Mission()

@Serializable
data class WateringMission(
    override val timestamp: Long,
    override val plant: Plant,
    val quantity: Int,
    override val _id: Int = ObjectId().hashCode(),
    override val resultData: MissionResultData? = null,
    override val timestampSentToClient: Int? = null,
) : Mission()