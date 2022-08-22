package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.worker.RefreshAsteroidWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/** Class to schedule background work of WorkerManager*/
class AsteroidApplication : Application(){
    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    //Create an initialization function that does not block the main thread:
    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    //Make a PeriodWorkRequest for RefreshAsteroidWork, which runs once every day
    private fun setupRecurringWork() {
        // Define constraints to prevent work from occurring when there is no network access or the device is low on battery.
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        //Make a PeriodWorkRequest for RefreshAsteroidWork, which runs once every day and add constraints
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshAsteroidWork>(1, TimeUnit.DAYS).
        setConstraints(constraints).build()

        //Schedule the work as unique:
        WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshAsteroidWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)

    }




}