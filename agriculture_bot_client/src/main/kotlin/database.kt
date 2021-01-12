import org.litote.kmongo.*
import org.litote.kmongo.KMongo

object Database {
    val client = KMongo.createClient(getDatabaseURI()) //get com.mongodb.MongoClient new instance
    val database = client.getDatabase("aggriculture_fog_project_db") //normal java driver usage
    val missionCollection = database.getCollection<Mission>("missionCollection") //KMongo extension method

    fun getDatabaseURI(): String {
        val uri = System.getenv("FOGMONGODATABASE")
        println("Database used:" + uri)
        assert(uri == null) { "You need to specify the FOGMONGODATABASE environment variable with the uri to the database!" }
        return uri
    }

    fun insertMission(mission: Mission) {
        missionCollection.insertOne(mission)
    }

    fun updateMission(mission: Mission) {
        missionCollection.updateOneById(mission._id, mission)
    }

    fun getOpenMissionsCount(): Int {
        return missionCollection.aggregate<Mission>(
            match(Mission::resultData eq null),
        ).count()
    }

    fun getAllOpenMissions(): List<Mission> {
        return missionCollection.aggregate<Mission>(
            match(Mission::resultData eq null),
        ).toList()
    }

    fun deleteAllClosedMissions() {
        missionCollection.deleteMany(Mission::resultData ne null)
    }

    fun getRandomOpenMissions(sampleSize: Int): List<Mission> {
        return missionCollection.aggregate<Mission>(
            sample(sampleSize),
            match(Mission::resultData eq null),
        ).toList()
    }

    fun getAllClosedMissions(): List<Mission> {
        return missionCollection.aggregate<Mission>(
            match(Mission::resultData ne null),
        ).toList()
    }

    fun updateTimeSentToClient(missions: List<Mission>) {
        val currentTimestamp = System.currentTimeMillis()
        missions.forEach {
            missionCollection.updateOne(
                Mission::_id eq it._id, setValue(Mission::timestampClosed, currentTimestamp)
            )
        }
    }

    fun updateMissionResultData(mission: Mission, missionResultData: MissionResultData) {
        missionCollection.updateOne(
            Mission::_id eq mission._id, setValue(Mission::resultData, missionResultData)
        )
    }

    fun reopenExpiredMissions() {
        val missionExpirationTimestamp: Long = System.currentTimeMillis() - 60000
        print(missionExpirationTimestamp)

        missionCollection.aggregate<Mission>(
            match(Mission::resultData ne null, Mission::timestampClosed lt missionExpirationTimestamp),
        )
            .toList()
            .forEach {
                missionCollection.updateOne(Mission::_id eq it._id, setValue(Mission::timestampClosed, null))
            }
    }
}


