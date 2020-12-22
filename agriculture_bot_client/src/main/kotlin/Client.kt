import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main() {
    Client().run()
}


class Client {

    fun run() {
        ZContext().use { context ->
            println("Connecting to hello world server")

            //  Socket to talk to server
            val socket = context.createSocket(SocketType.REQ)
            socket.connect("tcp://localhost:5555")
            while (!Thread.currentThread().isInterrupted) {
                val request = "Request Mission"
                socket.send(request.toByteArray(ZMQ.CHARSET), 0)
                val reply = socket.recv(0)
                println(
                        "Received " + String(reply, ZMQ.CHARSET)
                )
                Thread.sleep(500)
            }
        }
    }
}