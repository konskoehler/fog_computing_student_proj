import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import kotlin.random.Random

fun main() {
    Client().run()
}

@Serializable
data class ClientRequest(val openMissions: Int, val missionResultsList: List<Mission>)

class Client {
    private lateinit var socket: ZMQ.Socket

    fun run() {
        ZContext().use { context ->
            socket = context.createSocket(SocketType.REQ)
            socket.connect("tcp://localhost:5555")

            println("Connected to Server")

            while (!Thread.currentThread().isInterrupted) {
                socket.send(
                    Json.encodeToString(
                        ClientRequest(Database.getOpenMissionsCount(), Database.getAllClosedMissions())
                    ).toByteArray(ZMQ.CHARSET), 0
                )
                println("Request sent")

                val serverResponse: ServerResponse = Json.decodeFromJsonElement<ServerResponse>(
                    Json.parseToJsonElement(String(socket.recv(0), ZMQ.CHARSET)).jsonObject
                )
                println("Received: $serverResponse")

                // Delete all closed missions as soon as the server replied
                Database.deleteAllClosedMissions()

                if (serverResponse.missionList.isNotEmpty()) {
                    serverResponse.missionList.forEach {
                        Database.insertMission(it)
                    }
                }

                processOpenMissions()

                Thread.sleep(1000)
            }
        }
    }

    private fun processOpenMissions() {
        println("Beep Beep... Robot is doing some of its missions...")
        Database.getRandomOpenMissions(Random.nextInt(4))
            .forEach { mission ->
                when (mission) {
                    is InspectionMission -> processInspectionMission(mission)                  // InspectionMission was sent
                    is WateringMission -> processWateringMission(mission)                    // WateringMission was sent
                }
            }
    }

    private fun processInspectionMission(mission: Mission) {
        print(" Inspecting plants ...")
        Thread.sleep(200)
        Database.updateMissionResultData(mission, createInspectionData())
    }

    private fun processWateringMission(mission: Mission) {
        print(" Watering plants ...")
        Thread.sleep(300)
        Database.updateMissionResultData(mission, createWateringData())
    }
}