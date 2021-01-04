
import org.litote.kmongo.*
import org.litote.kmongo.KMongo

object Database {
    val uri =  System.getenv("FOGMONGODATABASE")
    val client = KMongo.createClient(uri) //get com.mongodb.MongoClient new instance
    val database = client.getDatabase("aggriculture_fog_project_db") //normal java driver usage
    val missionCollection = database.getCollection<Mission>("missionCollection") //KMongo extension method
    val missionResultDataCollection =
        database.getCollection<MissionResultData>("missionResultDataCollection") //KMongo extension method

    fun getOpenMissions(): List<Mission> {
        return missionCollection.find<Mission>().toList()
    }

    fun updateMission(mission: Mission) {
        mission._id?.let { missionCollection.updateOneById(mission._id!!, mission) }

    }

    fun getAllOpenMissions(): List<Mission> {
        return missionCollection.aggregate<Mission>(
            match(Mission::resultData eq null),
        ).toList()
    }

    fun getRandomOpenMissions(sampleSize: Int): List<Mission> {
        return missionCollection.aggregate<Mission>(
            sample(sampleSize),
            match(Mission::resultData eq null),
        ).toList()
    }

}

