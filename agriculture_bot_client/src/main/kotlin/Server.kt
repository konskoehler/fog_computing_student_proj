import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
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

class Server {
    private lateinit var socket: ZMQ.Socket

    private val missions: MutableList<Mission> =
        (plantList.indices).map { InspectionMission(currentTimeMillis(), plantList[it]) }.toMutableList()

    fun run() {
        ZContext().use { context ->
            socket = context.createSocket(SocketType.REP)
            socket.connect("tcp://localhost:5555")
            println("Server ready")
            while (!Thread.currentThread().isInterrupted) {
                runBlocking {
                    withTimeoutOrNull(250) {
                        val missionResultDataRequest: MissionResultData = Json.decodeFromJsonElement<MissionResultData>(
                            Json.parseToJsonElement(String(socket.recv(0), ZMQ.CHARSET)).jsonObject
                        )

                        when (missionResultDataRequest) {
                            is WateringResultData -> processInspectionResultData()              // client sends InspectionResultData
                            is InspectionResultData -> processWateringResultData()                // client sends WateringResultData
                        }
                    }
                    sendMission()
                }
            }
        }
    }


    private fun sendMission() {
        println("sending...")
        val missionIndex =
            when {
                missions.size > 0 -> nextInt(0, missions.size)
                else -> return              // all missions are done
            }
        val mission = missions[missionIndex]
        //val type = if (mission is InspectionMission) 0 else 1
        val response = Json.encodeToString(mission)
        println("Send: $response")
        socket.send(response.toByteArray(ZMQ.CHARSET), 0)
        missions.removeAt(missionIndex)
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