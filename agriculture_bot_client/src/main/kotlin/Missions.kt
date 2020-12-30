import kotlinx.serialization.Serializable
import org.kodein.db.model.Id

@Serializable
sealed class Mission(){
    abstract val timestamp: Long
    abstract val plant: Plant
    abstract val hash: Int
}

@Serializable
data class InspectionMission(override  val timestamp: Long, override val plant: Plant) : Mission() {
    @Id override val hash = this.hashCode()

}

@Serializable
data class WateringMission(override val timestamp: Long, override val plant: Plant, val quantity: Int):  Mission() {
    @Id override val hash = this.hashCode()
}