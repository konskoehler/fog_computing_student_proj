import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.lang.System.currentTimeMillis
import kotlin.random.Random.Default.nextInt


fun main() {
    Server().run()
}

@Serializable
data class ServerResponse(val type: Int, val obj: Mission)

class Server {

    private lateinit var socket: ZMQ.Socket
    private val missions: MutableList<Mission> = (plantList.indices).map { InspectionMission(currentTimeMillis(), plantList[it]) }.toMutableList()


    fun run() {
        ZContext().use { context ->
            socket = context.createSocket(SocketType.REP)
            socket.bind("tcp://*:5555")
            println("Server ready")
            while (!Thread.currentThread().isInterrupted) {
                // Block until a message is received
                val jsonReply = Json.parseToJsonElement(String(socket.recv(0), ZMQ.CHARSET)).jsonObject
                when (jsonReply["type"].toString().toInt()) {   //client requests new Mission
                    0 -> sendMission()                              // client sends empty Request
                    1 -> processInspectionResultData()              // client sends InspectionResultData
                    2 -> processWateringResultData()                // client sends WateringResultData
                    else -> continue
                }
            }
        }
    }

    private fun sendMission() {
        val missionIndex =
                when {
                    missions.size > 0 -> nextInt(0, missions.size)
                    else -> return              // all missions are done
                }
        val mission = missions[missionIndex]
        val type = if (mission is InspectionMission) 0 else 1
        val response = Json.encodeToString(ServerResponse(type, mission))
        println("Send: $response")
        socket.send(response.toByteArray(ZMQ.CHARSET), 0)
        missions.removeAt(missionIndex)
    }

    private fun processInspectionResultData() {
        TODO("Not yet implemented")
    }

    private fun processWateringResultData() {
        TODO("Not yet implemented")
    }

}