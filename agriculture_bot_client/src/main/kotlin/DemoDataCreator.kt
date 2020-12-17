import kotlin.random.Random

val gpsPostionList: List<GPSPosition> = listOf(
        GPSPosition(40.741895, -73.989308),
        GPSPosition(52.25656946732926, 13.753234042396873),
        GPSPosition(52.16860875622813, 13.817321068218082),
        GPSPosition(52.13445467414507, 14.03521668778929),
        GPSPosition(51.97524868527833, 14.016906252673),
        GPSPosition(51.93417311583675, 14.113952262869208),
        GPSPosition(51.845574804225464, 13.886901593377917),
        GPSPosition(51.84172858500746, 13.983947603574126)
)

val demoMissionsList: List<Missions> =

fun createInspectionData(): InspectionData {
    return InspectionData(gpsPostionList.random(), Random.nextFloat() * 14, Random.nextFloat())
}

fun createMissionData(): Missions {
    val randNum = Random.nextInt(0, 2)
    when (randNum) {
        0 -> return InspectionMission(gpsPostionList.random())
        1 -> return WateringMission(gpsPostionList.random(), Random.nextInt(0, 200))
    }


}