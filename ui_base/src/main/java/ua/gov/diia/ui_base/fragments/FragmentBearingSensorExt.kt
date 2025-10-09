package ua.gov.diia.ui_base.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.abs

fun Fragment.requestHeadingUpdates(updateInterval: Long = 2000): Flow<Double> {
    return callbackFlow {
        val sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (sensorGravity == null || sensorMagneticField == null) {
            close()
        } else {
            var gravity: FloatArray? = null
            var magnetic: FloatArray? = null
            var lastUpdateTime = 0L

            val callback = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    val debounce = (abs(System.currentTimeMillis() - lastUpdateTime) < updateInterval)
                    if (!debounce) {
                        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                            gravity = event.values
                        } else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                            magnetic = event.values
                        }

                        if (gravity != null && magnetic != null) {
                            val rotationMatrix = FloatArray(9)
                            if (SensorManager.getRotationMatrix(
                                    rotationMatrix,
                                    null,
                                    gravity,
                                    magnetic
                                )
                            ) {
                                val remappedRotationMatrix = FloatArray(9)
                                SensorManager.remapCoordinateSystem(
                                    rotationMatrix,
                                    SensorManager.AXIS_X, SensorManager.AXIS_Y,
                                    remappedRotationMatrix
                                )

                                val results = FloatArray(3)
                                SensorManager.getOrientation(remappedRotationMatrix, results)

                                var measuredBearing = (results[0] * 180 / Math.PI)
                                if (measuredBearing < 0) {
                                    measuredBearing += 360
                                }

                                lastUpdateTime = System.currentTimeMillis()
                                trySend(measuredBearing)
                            }
                        }
                    }
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

            }

            sensorManager.registerListener(callback, sensorGravity, SensorManager.SENSOR_DELAY_UI)
            sensorManager.registerListener(
                callback,
                sensorMagneticField,
                SensorManager.SENSOR_DELAY_UI
            )

            awaitClose {
                sensorManager.unregisterListener(callback, sensorGravity)
                sensorManager.unregisterListener(callback, sensorMagneticField)
            }
        }
    }
}