package com.example.geofrequencia

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.geofrequencia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MapViewModel by lazy {
        ViewModelProvider(this)[MapViewModel::class.java]
    }

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val testRequest = OneTimeWorkRequest.Builder(TestWork::class.java)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager = WorkManager.getInstance(this)

        workManager.beginWith(testRequest).enqueue()

        //initMonitoring()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSIONS && permissions.isNotEmpty()) {
            if (permissions.firstOrNull() == Manifest.permission.ACCESS_FINE_LOCATION && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                initMonitoring()
            } else {
                Toast.makeText(this, "Precisa de permissão", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    //Se o sistema não for permitido usar o FINE_LOCATION então solicita a permissão
    //Quando a permissão for concedida começa a monitorar a localização
    private fun initMonitoring() {
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS
            )
        }


        //workManager.enqueue(oneTimeWorkRequest)
        NotifService.defViewModel(viewModel)
        startService(Intent(this, NotifService::class.java))


    }

    //Checa se o sistema é permitido usar FINE_LOCATION
    private fun hasPermission(): Boolean {
        val granted = PackageManager.PERMISSION_GRANTED

        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == granted
    }

    private fun buildOneTimeWorkRemoteWorkRequest(
        componentName: ComponentName, listenableWorkerClass: Class<out ListenableWorker>
    ): OneTimeWorkRequest {

        // ARGUMENT_PACKAGE_NAME and ARGUMENT_CLASS_NAME are used to determine the service
        // that a Worker binds to. By specifying these parameters, we can designate the process a
        // Worker runs in.
        val data: Data = Data.Builder()
            .putString("ARGUMENT_PACKAGE_NAME", componentName.packageName)
            .putString("ARGUMENT_CLASS_NAME", componentName.className)
            .build()

        return OneTimeWorkRequest.Builder(listenableWorkerClass)
            .setInputData(data)
            .build()
    }


    companion object {
        private const val REQUEST_PERMISSIONS = 2
    }
}