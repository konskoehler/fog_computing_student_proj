import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main() {
    Client().run()
}

@Serializable
data class ClientRequest(val openMissions: Int, val missionResultsList: List<MissionResultData>?)

class Client {
    private lateinit var socket: ZMQ.Socket

    private val missionList = mutableListOf<Mission>()

    private val missionResultsList = mutableListOf<MissionResultData>(createInspectionData(), createInspectionData())

    fun run() {
        ZContext().use { context ->
            socket = context.createSocket(SocketType.REQ)
            socket.connect("tcp://localhost:5555")

            println("Connected to Server")

            while (!Thread.currentThread().isInterrupted) {
                socket.send(
                    Json.encodeToString(
                        ClientRequest(getOpenMissionCount(), getMissionResultsList())
                    ).toByteArray(ZMQ.CHARSET), 0
                )

                val missionRequest: Mission = Json.decodeFromJsonElement(
                    Json.parseToJsonElement(String(socket.recv(0), ZMQ.CHARSET)).jsonObject
                )
                println("Received: $missionRequest")
                missionList.add(0, missionRequest)

                if (missionList.isNotEmpty()) {
                    val mission: Mission = missionList.removeAt(0)
                    when (mission) {
                        is InspectionMission -> processInspectionMission()                  // InspectionMission was sent
                        is WateringMission -> processWateringMission()                    // WateringMission was sent
                    }
                }

                Thread.sleep(1000)
            }
        }
    }

    private fun getMissionResultsList(): List<MissionResultData>? {
        return missionResultsList
    }

    private fun getOpenMissionCount(): Int {
        return missionList.count()
    }

    private fun processInspectionMission() {
        return
        TODO("Not yet implemented")
    }

    private fun processWateringMission() {
        return
        TODO("Not yet implemented")
    }
}