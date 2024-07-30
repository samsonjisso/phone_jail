package com.samsonjisso.phonejail

import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val appModules = module {
    single { MainViewModel() }
}