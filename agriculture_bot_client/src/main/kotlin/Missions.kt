import kotlinx.serialization.Serializable
import org.litote.kmongo.newId

@Serializable
sealed class Mission() {
    abstract val timestamp: Long
    abstract val plant: Plant
    abstract val _id: String
    abstract var resultData: MissionResultData?
    abstract var timestampClosed: Int?
}

@Serializable
data class InspectionMission(
    override val timestamp: Long,
    override val plant: Plant,
    override val _id: String = newId<Mission>().toString(),
    override var resultData: MissionResultData? = null,
    override var timestampClosed: Int? = null,
) :
    Mission()

@Serializable
data class WateringMission(
    override val timestamp: Long,
    override val plant: Plant,
    val quantity: Int,
    override val _id: String = newId<Mission>().toString(),
    override var resultData: MissionResultData? = null,
    override var timestampClosed: Int? = null,
) : Mission()