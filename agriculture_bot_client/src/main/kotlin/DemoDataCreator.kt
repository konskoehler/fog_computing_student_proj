import java.lang.System.currentTimeMillis
import kotlin.random.Random

val plantList: List<Plant> = listOf(
    Plant(1L, GPSPosition(40.741895, -73.989308)),
    Plant(2L, GPSPosition(52.25656946732926, 13.753234042396873)),
    Plant(3L, GPSPosition(52.16860875622813, 13.817321068218082)),
    Plant(4L, GPSPosition(52.13445467414507, 14.03521668778929)),
    Plant(5L, GPSPosition(51.97524868527833, 14.016906252673)),
    Plant(6L, GPSPosition(51.93417311583675, 14.113952262869208)),
    Plant(7L, GPSPosition(51.845574804225464, 13.886901593377917)),
    Plant(8L, GPSPosition(51.84172858500746, 13.983947603574126))
)

fun createInspectionData(): InspectionResultData {
    return InspectionResultData(currentTimeMillis(), Random.nextFloat() * 14, Random.nextFloat())
}

fun createWateringData(): WateringResultData {
    return WateringResultData(currentTimeMillis(), true)
}

fun createMissionDemoDataAndInsertIntoDB(sampleSize: Int) {
    for (i in 1..sampleSize) {
        val randNum = Random.nextInt(0, 2)
        when (randNum) {
            0 -> return Database.insertMission(InspectionMission(currentTimeMillis(), plantList.random()))
            else -> return Database.insertMission(
                WateringMission(
                    currentTimeMillis(),
                    plantList.random(),
                    Random.nextInt(0, 200)
                )
            )
        }
    }
    println("Amount of demo samples added to database: $sampleSize")
}
