import kotlinx.serialization.Serializable

@Serializable
sealed class Mission() {
    abstract val timestamp: Long
    abstract val plant: Plant
}

@Serializable
class InspectionMission(override  val timestamp: Long, override val plant: Plant) : Mission()

@Serializable
class WateringMission(override val timestamp: Long, override val plant: Plant, val quantity: Int):  Mission()