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
            socket = createReqSocket(context)
            println("Connected to Server")


            while (true) runBlocking {
                socket.send(
                        Json.encodeToString(
                                ClientRequest(getOpenMissionCount(), getMissionResultsList())
                        ).toByteArray(ZMQ.CHARSET), 0
                )
                println("Request sent")

                val response = socket.recv(0)

                val validServerResponse: ServerResponse = if (response == null) {
                    println("Timeout")
                    socket.close()
                    socket = createReqSocket(context)
                    return@runBlocking
                } else {
                    Json.decodeFromJsonElement(
                            Json.parseToJsonElement(String(response, ZMQ.CHARSET)).jsonObject
                    )
                }
                println("Received: $validServerResponse")
                validServerResponse.missionList?.let { missionList.addAll(it) }

                if (missionList.isNotEmpty()) {
                    when (missionList.removeAt(0)) {
                        is InspectionMission -> processInspectionMission()                  // InspectionMission was sent
                        is WateringMission -> processWateringMission()                    // WateringMission was sent
                    }
                }
                delay(1000)
            }
        }
    }

    private fun createReqSocket(context: ZContext): ZMQ.Socket {
        val socket = context.createSocket(SocketType.REQ)
        socket.receiveTimeOut = 5000
        socket.connect("tcp://localhost:5555")
        return socket
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