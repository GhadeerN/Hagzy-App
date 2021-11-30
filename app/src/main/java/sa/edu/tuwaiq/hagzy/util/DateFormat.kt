package sa.edu.tuwaiq.hagzy.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter



//"dateupload": "1610921222",
//"datetaken": "2017-03-12 05:41:00"

private const val TAG = "DateFormat"
class DateFormat {


    @RequiresApi(Build.VERSION_CODES.O)
     fun dateFormatted(date: String): String {
        var dateLong = date.toLong()
        val dateTime: LocalDateTime = LocalDateTime.ofInstant(
            dateLong?.let {
                Instant.ofEpochSecond(it) }, ZoneId.systemDefault())

        // The pattern letters means: E -> day name, MMM -> Month name, d -> day of month number, y -> the year
        // Resource: https://developer.android.com/reference/kotlin/java/time/format/DateTimeFormatter
        return dateTime.format(DateTimeFormatter.ofPattern("E, MMM d, y"))
    }

}