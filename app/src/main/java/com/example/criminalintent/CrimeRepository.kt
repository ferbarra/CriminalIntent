package com.example.criminalintent

import android.content.Context
import android.os.strictmode.InstanceCountViolation
import androidx.lifecycle.LiveData
import androidx.room.Room
import database.CrimeDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes() : LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(uuid: UUID) : LiveData<Crime?> = crimeDao.getCrime(uuid)

    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE != null) return // no need to do anything if already initialized

            INSTANCE = CrimeRepository(context)
        }

        fun get() : CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}