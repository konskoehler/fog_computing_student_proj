import org.kodein.db.DB
import org.kodein.db.TypeTable
import org.kodein.db.impl.open

object database {
    val db = DB.open("",
        TypeTable {
            root<Mission>()
                .sub<InspectionMission>()
                .sub<WateringMission>()
            root<MissionResultData>()
                .sub<InspectionResultData>()
                .sub<WateringResultData>()
        })

    fun pushMission(mission: Mission){
        val key = db.put(mission)
    }

    fun deleteMission(key: String) {
        //ToDo: Implement
    }
}