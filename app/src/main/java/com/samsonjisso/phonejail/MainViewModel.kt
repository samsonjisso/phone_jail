package com.samsonjisso.phonejail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel : ViewModel() {

    private  suspend fun runAsRoot(command: String): Pair<Int, String> {
        return withContext(Dispatchers.IO) {
            try {
                val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
                val exitCode = process.waitFor()
                val output = process.inputStream.bufferedReader().use { it.readText() }
                Pair(exitCode, output)
            } catch (e: Exception) {
                e.printStackTrace()
                Pair(-1, e.message ?: "Error executing root command")
            }
        }
    }

    suspend fun removeEnems(): String {
        val enemList = listOf(
            "com.android.chrome",
            "com.android.vending",
            "com.google.android.youtube",
            "com.sec.android.app.samsungapps",
            "com.facebook.katana",
            "org.mozilla.firefox",
            "org.telegram.messenger",
            "com.chess",
            "com.zhiliaoapp.musically",
            "com.google.android.googlequicksearchbox"
        )
        val unInstallCommand = "pm uninstall -k --user 0"
        val installCommand = "pm install-existing"

        if (processTime()) {
            enemList.map {
                runAsRoot("$installCommand $it")
            }
            return "Enjoy Your Day"
        } else {
            enemList.map {
                runAsRoot("$unInstallCommand $it")
            }
            return "Enjoy Your Night, Remember Your Value and Work Hard"
        }
    }

    private fun processTime(): Boolean {
        val currentDate = LocalDate.now()
        val currentDateTime = LocalTime.now()
        val hour = currentDateTime.hour

        return when (currentDate.dayOfWeek) {
            DayOfWeek.SUNDAY -> false
            DayOfWeek.SATURDAY -> hour in 7..12
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY -> hour in 7..19

            else -> false
        }
    }
//    suspend fun checkAndRequestRootPermission(): Boolean {
//        return withContext(Dispatchers.IO) {
//            Shell.getShell().isRoot
//        }
//    }
}