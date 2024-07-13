package com.example.lighttorch

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var btnPowerLight: Button
    lateinit var btnSoSsignal: Button
    lateinit var btnStop: Button
    lateinit var txtTv: TextView

    var isFlashPoweringUp = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_main)

        btnPowerLight = findViewById(R.id.btnONOFFlight)
        btnSoSsignal = findViewById(R.id.btnsos)
        btnStop = findViewById(R.id.btnStopLight)
        txtTv = findViewById(R.id.statusTV)

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        btnPowerLight.setOnClickListener {
            txtTv.text = "Flashlight status: Working"
            try {
                val cameraIdList = cameraManager.cameraIdList
                for (cameraId in cameraIdList) {
                    val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                    val flashavaible = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

                    if (flashavaible == true) {
                        isFlashPoweringUp = !isFlashPoweringUp
                        cameraManager.setTorchMode(cameraId, isFlashPoweringUp)
                    }

                }

            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        btnSoSsignal.setOnClickListener {
            txtTv.text = "Flashlight status: SOS signal"
            val pattern = longArrayOf(1000, 1000, 1000, 3000, 1000, 3000, 1000, 1000, 1000, 3000, 1000, 3000, 1000, 3000)//SoS pattern
            try {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
                for (i in pattern.indices step 2) {
                    Thread.sleep(pattern[i])
                    cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
                    Thread.sleep(pattern[i + 1])
                    cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
                }
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
            }catch (e: CameraAccessException) {
                e.printStackTrace()
            }catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        btnStop.setOnClickListener {
            txtTv.text = "Flashlight status: Stop light"
            if (cameraManager.cameraIdList.isNotEmpty()) {
                try {
                    cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
                }catch (e:CameraAccessException){
                    e.printStackTrace()
                }
            }
        }
    }
}
