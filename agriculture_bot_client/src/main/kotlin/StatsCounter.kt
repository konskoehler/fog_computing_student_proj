object StatsCounter {
    var sentSinceStart: Int = 0
    var receivedSinceStart: Int = 0
    var closedSinceStart: Int = 0
    var timeoutCount: Int = 0

    fun printServerStats() {
        print("Open Missions: ${Database.getOpenMissionsCount()} | Missions Sent Since Start: $sentSinceStart  | Received Mission Results: $closedSinceStart \r")
    }

    fun printClientStats() {
        print("Open Missions: ${Database.getOpenMissionsCount()} | Missions Received: $receivedSinceStart  | Mission Results Sent: $sentSinceStart | Timeouts: $timeoutCount \r")
    }

    fun increaseSentSinceStartCount(count: Int) {
        sentSinceStart += count
    }

    fun increaseClosedSinceStart(count: Int) {
        closedSinceStart += count
    }

    fun increaseReceivedSinceStart(count: Int) {
        receivedSinceStart += count
    }

    fun increaseTimeoutCount(count: Int) {
        timeoutCount += count
    }
}