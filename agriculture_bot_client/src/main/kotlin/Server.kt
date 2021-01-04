import Database.getRandomOpenMissions
import Database.missionCollection
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
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
data class ServerResponse(val missionList: List<Mission>?)

class Server {
    private lateinit var socket: ZMQ.Socket

    fun run() {
        ZContext().use { context ->
            socket = context.createSocket(SocketType.REP)
            socket.bind("tcp://*:5555")
        }

        plantList.forEach {
            missionCollection.insertOne(InspectionMission(currentTimeMillis(), it))
        }

        println("Server ready")

        while (!Thread.currentThread().isInterrupted) {
            processClientRequest(
                Json.decodeFromJsonElement<ClientRequest>(
                    Json.parseToJsonElement(String(socket.recv(0), ZMQ.CHARSET)).jsonObject
                )
            )

            sendMission()
            Thread.sleep(730)
        }
    }

    private fun processClientRequest(clientRequest: ClientRequest) {
        println("Processing Request")
        clientRequest.missionResultsList?.let { list ->
            list.forEach {
                when (it) {
                    is WateringResultData -> processInspectionResultData()              // client sends InspectionResultData
                    is InspectionResultData -> processWateringResultData()                // client sends WateringResultData
                }
            }
        }
    }

    private fun sendMission() {
        println("sending...")
        val serverResponse = ServerResponse(getRandomOpenMissions(nextInt(4)))
        val response = Json.encodeToString<ServerResponse>(serverResponse)
        println("Send: $response")
        socket.send(response.toByteArray(ZMQ.CHARSET), 0)
    }

    fun processInspectionResultData() {
        println("Server received inspection result data")
        //TODO("Not yet implemented")
    }

    private fun processWateringResultData() {
        println("Server received watering result data")

        //TODO("Not yet implemented")
    }
}