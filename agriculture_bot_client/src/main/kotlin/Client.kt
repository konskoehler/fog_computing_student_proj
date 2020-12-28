import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main() {
    Client().run()
}

class Client {
    private lateinit var socket: ZMQ.Socket

    private val missions = mutableListOf<Mission>()

    private val inspectionResultData: InspectionResultData? = null

    private val wateringResultData: WateringResultData? = null

    fun run() {
        ZContext().use { context ->
            socket = context.createSocket(SocketType.REQ)
            socket.connect("tcp://localhost:5555")
            println("Connected to Server")

            while (!Thread.currentThread().isInterrupted) {

                val request =
                        when {                                                                          // request new Mission
                            missions.isEmpty() -> ClientRequest(0, null)                            // 0 = send Empty Request
                            inspectionResultData != null -> ClientRequest(1, inspectionResultData)      // 1 = send InspectionData
                            wateringResultData != null -> ClientRequest(2, wateringResultData)          // 2 = send WateringData
                            else -> throw IllegalStateException()
                        }

                println("Sent: " + Json.encodeToString(request))
                socket.send(Json.encodeToString(request).toByteArray(ZMQ.CHARSET), 0)

                val jsonReply = Json.parseToJsonElement(String(socket.recv(0), ZMQ.CHARSET)).jsonObject
                println("Received: $jsonReply")
                when (request) {
                    is InspectionMission -> processInspectionMission()                  // InspectionMission was sent
                    is WateringMission -> processWateringMission()                    // WateringMission was sent
                    else -> IllegalStateException()
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