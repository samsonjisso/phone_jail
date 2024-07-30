package com.samsonjisso.phonejail

import org.koin.dsl.module

val appModules = module {
    single { MainViewModel() }
}