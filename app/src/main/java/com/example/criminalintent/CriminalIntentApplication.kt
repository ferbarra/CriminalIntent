package com.example.criminalintent

import android.app.Application
import androidx.room.Room
import database.CrimeDatabase


class CriminalIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}