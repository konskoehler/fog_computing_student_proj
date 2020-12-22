import kotlinx.serialization.Serializable

@Serializable
data class InspectionData(val timestamp: Long, val gpsPosition: GPSPosition, val pHLevel: Float, val soilMoisture: Float)
