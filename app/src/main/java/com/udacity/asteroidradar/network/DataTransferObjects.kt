package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

//An extension function that converts from data transfer objects to database objects
fun ArrayList<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return this.map {
        DatabaseAsteroid (
            id = it.id,
            codeName = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity= it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous =it.isPotentiallyHazardous)
    }.toTypedArray()
}