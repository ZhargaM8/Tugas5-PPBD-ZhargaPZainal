package com.example.ppbd_tugas3

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ppbd_tugas3.ui.theme.PPBDTugas3Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PPBDTugas3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AgeCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun AgeCalculatorScreen(modifier: Modifier = Modifier) {
    var birthday by remember { mutableStateOf<Date?>(null) }
    var ageText by remember { mutableStateOf("Pilih tanggal lahir mu!") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val indonesianLocale = Locale("in", "ID")
    val dateFormatter = SimpleDateFormat("dd MMMM yyyy", indonesianLocale)
    val jakartaTimeZone = TimeZone.getTimeZone("Asia/Jakarta")
    LaunchedEffect(birthday) {
        if (birthday != null) {
            coroutineScope.launch {
                while (true) {
                    birthday?.let { birth ->
                        val jakartaCalendar = Calendar.getInstance(jakartaTimeZone)
                        val now = jakartaCalendar.time
                        val diffInMillis = now.time - birth.time

                        val totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                        val years = totalDays / 365
                        val daysLeft = totalDays % 365
                        val months = daysLeft / 30.42
                        val remainingDays = daysLeft % 30.42
                        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60

                        ageText = buildString {
                            append("Umur kamu adalah ")
                            append(years).append(" tahun, ")
                            append(months.toInt()).append(" bulan, ")
                            append(remainingDays.toInt()).append(" hari, ")
                            append(hours).append(" jam, ")
                            append(minutes).append(" menit, ")
                            append(seconds).append(" detik")
                        }
                    }
                    delay(1000L)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1A237E))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kalkulator Umur",
            fontSize = 32.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                val calendar = Calendar.getInstance(jakartaTimeZone)
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        val selectedDate = Calendar.getInstance(jakartaTimeZone).apply {
                            set(year, month, day, 0, 0, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        birthday = selectedDate.time
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            )
        ) {
            Text("Pilih Tanggal Lahir", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = birthday?.let { dateFormatter.format(it) } ?: "Belum ada tanggal dipilih",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = ageText,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}