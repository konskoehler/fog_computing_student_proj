import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
            socket = createReqSocket(context)
            println("Connected to Server")

            while (true) runBlocking {
                var closedMissions = Database.getAllClosedMissions()

                socket.send(
                    Json.encodeToString(
                        ClientRequest(Database.getOpenMissionsCount(), closedMissions)
                    ).toByteArray(ZMQ.CHARSET), 0
                )
                //println("Request sent")

                val response = socket.recv(0)

                val validServerResponse: ServerResponse = if (response == null) {
                    StatsCounter.increaseTimeoutCount(1)
                    StatsCounter.printClientStats()

                    socket.close()
                    socket = createReqSocket(context)
                    return@runBlocking
                } else {
                    StatsCounter.increaseSentSinceStartCount(closedMissions.size)

                    Json.decodeFromJsonElement(
                        Json.parseToJsonElement(String(response, ZMQ.CHARSET)).jsonObject
                    )
                }
                //println("Received: $validServerResponse")

                // Delete all closed missions as soon as the server replied
                Database.deleteAllClosedMissions()

                if (validServerResponse.missionList.isNotEmpty()) {
                    StatsCounter.increaseReceivedSinceStart(validServerResponse.missionList.size)
                    validServerResponse.missionList.forEach {
                        Database.insertMission(it)
                    }
                }

                StatsCounter.printClientStats()
                processOpenMissions()
                delay(200)
            }
        }
    }

    private fun createReqSocket(context: ZContext): ZMQ.Socket {
        val socket = context.createSocket(SocketType.REQ)
        socket.receiveTimeOut = 5000
        socket.connect("tcp://localhost:5555")
        return socket
    }

    private fun processOpenMissions() {
        //println("Beep Beep... Robot is doing some of its missions...")
        Database.getRandomOpenMissions(Random.nextInt(4))
            .forEach { mission ->
                when (mission) {
                    is InspectionMission -> processInspectionMission(mission)                  // InspectionMission was sent
                    is WateringMission -> processWateringMission(mission)                    // WateringMission was sent
                }
            }
    }

    private fun processInspectionMission(mission: Mission) {
        //print(" Inspecting plants ...")
        Thread.sleep(200)
        Database.updateMissionResultData(mission, createInspectionData())
    }

    private fun processWateringMission(mission: Mission) {
        //print(" Watering plants ...")
        Thread.sleep(300)
        Database.updateMissionResultData(mission, createWateringData())
    }

    private fun printServerProgress() {
        val total = 10
        var openMissionProgressString: String =
            "#".repeat(Database.getOpenMissionsCount()) + "-".repeat(total - Database.getOpenMissionsCount())
        print(openMissionProgressString + "\r ")
    }
}