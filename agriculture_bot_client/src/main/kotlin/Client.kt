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

class Client {
    private lateinit var senderSocket: ZMQ.Socket
    private lateinit var receiverSocket: ZMQ.Socket

    private val missionList = mutableListOf<Mission>()

    private val missionResultsDataList = mutableListOf<MissionResultData>(createInspectionData())

    fun run() {
        ZContext().use { context ->
            senderSocket = context.createSocket(SocketType.REQ)
            senderSocket.connect("tcp://localhost:5555")

            receiverSocket = context.createSocket(SocketType.REP)
            receiverSocket.connect("tcp://localhost:5556")
            println("Connected to Server")

            while (!Thread.currentThread().isInterrupted) {
                if (missionResultsDataList.isNotEmpty()) {
                    val singleMissionResult = missionResultsDataList.removeAt(0)
                    println("Sent: " + Json.encodeToString(singleMissionResult))
                    senderSocket.send(Json.encodeToString(singleMissionResult).toByteArray(ZMQ.CHARSET), 0)
                }

                val missionRequest: Mission = Json.decodeFromJsonElement(
                    Json.parseToJsonElement(String(receiverSocket.recv(0), ZMQ.CHARSET)).jsonObject
                )
                println("Received: $missionRequest")
                missionList.add(0, missionRequest)

                if(missionList.isNotEmpty()) {
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

    private fun processInspectionMission() {
        return
        TODO("Not yet implemented")
    }

    private fun processWateringMission() {
        return
        TODO("Not yet implemented")
    }
}