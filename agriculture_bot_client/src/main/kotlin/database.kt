import org.kodein.db.DB
import org.kodein.db.TypeTable
import org.kodein.db.find
import org.kodein.db.impl.open
import org.kodein.db.useModels
import org.litote.kmongo.KMongo

object database {
    val client = KMongo.createClient() //get com.mongodb.MongoClient new instance
    val database = client.getDatabase("test") //normal java driver usage
    val missionsCollection = database.getCollection<Missions>() //KMongo extension method
    val missionResultDataCollection = database.getCollection<Missions>() //KMongo extension method
}