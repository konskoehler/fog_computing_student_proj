import org.kodein.db.DB
import org.kodein.db.TypeTable
import org.kodein.db.find
import org.kodein.db.impl.open
import org.kodein.db.useModels

object database {
    val db = DB.open("missions_db",
        TypeTable {
            root<Mission>()
                .sub<InspectionMission>()
                .sub<WateringMission>()
            root<MissionResultData>()
                .sub<InspectionResultData>()
                .sub<WateringResultData>()
        })

    fun pushMission(mission: InspectionMission){
        val key = db.put(InspectionMission)
    }

    fun fetchMissions(): List<Mission> {
        return db.find<Mission>().all().useModels { it.toList() }
    }
}