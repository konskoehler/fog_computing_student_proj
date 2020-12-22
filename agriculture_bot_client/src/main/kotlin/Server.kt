import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import kotlin.random.Random.Default.nextInt


fun main() {
    Server().run()
}

class Server {

    private val missions = (0..20).toMutableList()

    fun run() {
        ZContext().use { context ->
            // Socket to talk to clients
            val socket = context.createSocket(SocketType.REP)
            socket.bind("tcp://*:5555")

            while (!Thread.currentThread().isInterrupted) {

                val missionInt =
                        when {
                            missions.size > 1 -> nextInt(0, missions.size - 1)
                            missions.size == 1 -> 0
                            else -> continue
                        }


                // Block until a message is received
                val reply = socket.recv(0)

                // Print the message
                println(
                        "Received: [" + String(reply, ZMQ.CHARSET) + "]"
                )

                // Send a response
                val response = "Do Mission ${missions[missionInt]}"
                socket.send(response.toByteArray(ZMQ.CHARSET), 0)
                missions.removeAt(missionInt)
            }
        }
    }
}