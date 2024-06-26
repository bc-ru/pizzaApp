package com.example.pizzaapp

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalTime
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import android.graphics.Path
import android.graphics.RectF
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextField
import androidx.navigation.NavHostController
import com.example.pizzaapp.data.NameEntity
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun MainPage(
    navController: NavHostController,
    mainViewModel : MainViewModel,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()

    val weatherList = remember { mainViewModel.weatherList }

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            getWeatherData(
                weatherViewModel = mainViewModel,
                context = context
            )
        }
    }

    Box(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#C9E1F2")))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
//                        verticalArrangement = Arrangement.SpaceBetween
        ) {
            upRow(
                if (weatherList.value.isNotEmpty()) weatherList.value[mainViewModel.weatherSelected.intValue].curTemp else "",
                if (weatherList.value.isNotEmpty()) weatherList.value[mainViewModel.weatherSelected.intValue].conditionIcon else "",
                )
            Courusel(mainViewModel)
            Clock()
            ButtonPlus(navController)
            ReminderText()
            ReminderList(mainViewModel)
        }
    }
}

data class ReminderItemSample(val text: String, val timeStart: String, val timeEnd: String)

@Composable
fun ReminderList(mainViewModel : MainViewModel) {
    val itemsList = mainViewModel.itemsList.collectAsState(initial = emptyList())


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(itemsList.value) { reminderItem ->
            ReminderListItem(
                item = reminderItem,
                mainViewModel = mainViewModel,
                onLocationSelected = { latLng ->
                    // Обработайте выбранное местоположение здесь
                    // Например, сохраните его в базе данных или используйте для других целей
                }
            )
        }
    }
}

@Composable
fun MapScreen(onLocationSelected: (LatLng) -> Unit) {
    val mapProperties = remember { MapProperties() }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(55.755826, 37.6173), 10f)
    }
    val markerState = remember { mutableStateOf<MarkerState?>(null) }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        properties = mapProperties,
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerState.value?.let {
                it.position = latLng
            } ?: run {
                markerState.value = MarkerState(position = latLng)
            }
            onLocationSelected(latLng)
        }
    ) {
        markerState.value?.let { marker ->
            Marker(
                state = marker,
                title = "Выбранное местоположение",
                snippet = "Нажмите здесь, чтобы выбрать это место"
            )
        }
    }
}


@Composable
private fun ReminderText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        Row(
//                                modifier = Modifier.fillMaxWidth(),
            //                            horizontalArrangement = Arrangement.End
        )
        {
            Text(
                text = "Напоминание", fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 0.sp
            )
        }
        Row {
            Text(text = "Не забудьте сделать завтра")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderListItem(
    item: NameEntity,
    mainViewModel: MainViewModel,
    onLocationSelected: (LatLng) -> Unit,
) {


    val cardStyle = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
//            color = Color(android.graphics.Color.parseColor("#BCA1BE"))
        color = Color.White
    )

    val openDialog = remember { mutableStateOf(false) }
    val selectedLocation = remember { mutableStateOf<LatLng?>(null) }


    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(3.5.dp)  // Отступы снаружи карточки
                .clickable(onClick = { openDialog.value = true }),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),  // Эффект "над экраном"
        ) {
            Box(
                modifier = Modifier
                    .background(Color(android.graphics.Color.parseColor("#8572FF")))
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                        contentDescription = "фыПЦФУАВЫМЧА",
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                    )
                    Column (
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.text,
                            style = cardStyle
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_alarm_24),
                                contentDescription = "фыПЦФУАВЫМЧА",
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Text(
                                text = "12:00 - 16:00",
                            )
                        }
                    }
                    IconButton(
                        onClick = {mainViewModel.deleteItem(item)}
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }

                }
            }
        }
    }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            content = {
                Column(modifier = Modifier
                    .background(Color.White)
                    .padding(20.dp)) {
                    TextField(value = item.text, onValueChange = { newText ->
                        // Обновите значение текста элемента здесь
                        mainViewModel.updateItemText(item.copy(text = newText))
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Выберите местоположение на карте.")
                    Spacer(modifier = Modifier.height(16.dp))
//                    GoogleMap(
//                        modifier = Modifier.height(200.dp),
//                        onMapLongClick = { latLng ->
//                            selectedLocation.value = latLng
//                            onLocationSelected(latLng)
//                            openDialog.value = false
//                        }
//                    )
                    MapScreen(onLocationSelected = { latLng -> })
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                            onClick = {
                                openDialog.value = false
                            }
                        ) {
                            Text("Сохранить")
                        }
                    Button(onClick = { openDialog.value = false }) {
                        Text("Закрыть")
                    }
                }
            }
        )
    }
}

@Composable
private fun ButtonPlus(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    )
    {
//        Icon(
//            Icons.Filled.Add, contentDescription = "фыПЦФУАВЫМЧА",
//            modifier = Modifier
//                .size(70.dp)
//                .padding(end = 15.dp)
////                                    .border(2.dp, Color.White, CircleShape)
//                .clip(CircleShape)
//                .background(Color.White)
//        )
        IconButton(
            onClick = {
// Возврат на предыдущий экран (назад)
                navController.navigate("SecondPage")
            },
            modifier = Modifier
                .size(70.dp)
                .padding(end = 15.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "фыПЦФУАВЫМЧА")
        }
    }
}

@Composable
fun WeatherListItem(
    id: Int,
    modifier: Modifier,
    day: String,
    weatherViewModel: MainViewModel
) {
    Box(
//        modifier = Modifier.padding(15.dp)
        modifier = modifier
            .clickable(onClick = {
                weatherViewModel.weatherSelected.intValue = id
            }),
    ) {
        Column {
            Text(
                text = day.slice(8..9)
            )
            Text(
                text = getDayOfWeekFromDate(day)
            )
        }
    }
}

@Composable
fun Courusel(
    weatherViewModel: MainViewModel
) {
    val weatherList = remember { weatherViewModel.weatherList }

    var modifier: Modifier

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        items(7) {
            modifier = if (weatherViewModel.weatherSelected.intValue == it) {
                Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(15.dp)
            } else {
                Modifier.padding(15.dp)
            }

            if (weatherList.value.isNotEmpty())
                WeatherListItem(
                    it,
                    modifier,
                    day = weatherList.value[it].day,
                    weatherViewModel
                )
        }

//        var value = 18
////        var value = weatherList.value[0].day.slice(7..9).toInt()
//        val numbersMap = mapOf(
//            "1" to "Пн",
//            "2" to "Вт",
//            "3" to "Ср",
//            "4" to "Чт",
//            "5" to "Пт",
//            "6" to "Сб",
//            "7" to "Вс"
//        )
//        for (i in 1..7) {
//            if (value == 21) {
//                Box(
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .background(Color.White)
//                        .padding(15.dp),
//                ) {
//                    Column {
//                        Text(
//                            text = value.toString(), color = Color.Red
//                        )
//                        numbersMap[i.toString()]?.let {
//                            Text(
//                                text = it, color = Color.Red
//                            )
//                        }
//                    }
//                }
//            } else {
//                Box(
//                    modifier = Modifier.padding(15.dp)
//                ) {
//                    Column {
//                        Text(
//                            text = value.toString()
//                        )
//                        numbersMap[i.toString()]?.let {
//                            Text(
//                                text = it
//                            )
//                        }
//                    }
//                }
//            }
//            value++
//        }
    }
}
@Composable
private fun Clock() {
    Box(
        modifier = Modifier
//                            .height(350.dp)
//                            .width(500.dp)
            .padding(vertical = 120.dp)
            .fillMaxWidth(0.9f),
//                            .background(Color.Black)
        contentAlignment = Alignment.Center,

        ) {
        AnalogClockComposable()
    }
}
@Composable
fun AnalogClockComposable(
//    modifier: Modifier = Modifier,
    modifier: Modifier = Modifier.fillMaxWidth(0.9f),
    minSize: Dp = 64.dp,
    time: LocalTime = LocalTime.now(),
    isClockRunning: Boolean = true
) {

    var seconds by remember { mutableStateOf(time.second) }
    var minutes by remember { mutableStateOf(time.minute) }
    var hours by remember { mutableStateOf(time.hour) }

    var hourAngle by remember {
        mutableStateOf(0.0)
    }

    LaunchedEffect(key1 = minutes) {
        //Just putting this in LaunchedEffect so that we are going only to calculate
        //the angle when the minutes change
        hourAngle = (minutes / 60.0 * 30.0) - 90.0 + (hours * 30)
    }

    LaunchedEffect(isClockRunning) {
        while (isClockRunning) {
            seconds += 1
            if (seconds > 60) {
                seconds = 1
                minutes++
            }
            if (minutes > 60) {
                minutes = 1
                hours++
            }
            delay(1000)
        }
    }

    BoxWithConstraints {

        //This is the estate we are going to work with
        val width = if (minWidth < 1.dp) minSize else minWidth
        val height = if (minHeight < 1.dp) minSize else minHeight

        Canvas(
            modifier = modifier
                .size(width, height)
        ) {

            //lets draw the circle now
            //but before we do that lets calculate the radius,
            //which is going to be 40% of the radius so we can achieve responsiveness
            val radius = size.width * .4f
            drawCircle(
                color = Color.Black,
                style = Stroke(width = radius * .05f /* 5% of the radius */),
                radius = radius,
                center = size.center
            )

            //The degree difference between the each 'minute' line
            val angleDegreeDifference = (360f / 60f)

            //drawing all lines
            (1..60).forEach {
                val angleRadDifference =
                    (((angleDegreeDifference * it) - 90f) * (PI / 180f)).toFloat()
                val lineLength = if (it % 5 == 0) radius * .85f else radius * .93f
                val lineColour = if (it % 5 == 0) Color.Black else Color.Gray
                val startOffsetLine = Offset(
                    x = lineLength * cos(angleRadDifference) + size.center.x,
                    y = lineLength * sin(angleRadDifference) + size.center.y
                )
                val endOffsetLine = Offset(
                    x = (radius - ((radius * .05f) / 2)) * cos(angleRadDifference) + size.center.x,
                    y = (radius - ((radius * .05f) / 2)) * sin(angleRadDifference) + size.center.y
                )
                drawLine(
                    color = lineColour,
                    start = startOffsetLine,
                    end = endOffsetLine
                )
                if (it % 5 == 0) {
                    //here we are using the native canvas (native canvas is the traditional one we use dto work with the views), so that we can draw text on the canvas
                    drawContext.canvas.nativeCanvas.apply {
                        val positionX = (radius * .75f) * cos(angleRadDifference) + size.center.x
                        val positionY = (radius * .75f) * sin(angleRadDifference) + size.center.y
                        val text = (it / 5).toString()
                        val paint = Paint()
                        paint.textSize = radius * .15f
                        paint.color = android.graphics.Color.GRAY
                        val textRect = Rect()
                        paint.getTextBounds(text, 0, text.length, textRect)

                        drawText(
                            text,
                            positionX - (textRect.width() / 2),
                            positionY + (textRect.width() / 2),
                            paint
                        )
                    }
                }
            }

            //now draw the center of the screen :O
            drawCircle(
                color = Color.Black,
                radius = radius * .02f, //only 2% of the main radius
                center = size.center
            )

            //hour hand
            drawLine(
                color = Color.Black,
                start = size.center,
                end = Offset(
                    //don't forget, the hourAngle is calculated in the one of the LaunchedEffects
                    x = (radius * .55f) * cos((hourAngle * (PI / 180)).toFloat()) + size.center.x,
                    y = (radius * .55f) * sin((hourAngle * (PI / 180)).toFloat()) + size.center.y
                ),
                strokeWidth = radius * .02f,
                cap = StrokeCap.Square
            )

//          minutes hand - just dividing the seconds with 60 and multiplying it by 6 degrees (which is the difference between the lines)
            // subtracting 90 as the 0degrees is actually at 3 o'clock
            val minutesAngle = (seconds / 60.0 * 6.0) - 90.0 + (minutes * 6.0)
            drawLine(
                color = Color.Black,
                start = size.center,
                end = Offset(
                    x = (radius * .7f) * cos((minutesAngle * (PI / 180)).toFloat()) + size.center.x,
                    y = (radius * .7f) * sin((minutesAngle * (PI / 180)).toFloat()) + size.center.y
                ),
                strokeWidth = radius * .01f,
                cap = StrokeCap.Square
            )

            //seconds hand
            drawLine(
                color = Color.Magenta,
                start = size.center,
                end = Offset(
                    x = (radius * .9f) * cos(seconds.secondsToRad()) + size.center.x,
                    y = (radius * .9f) * sin(seconds.secondsToRad()) + size.center.y
                ),
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round
            )

            //paused text
            if (!isClockRunning) {
                drawContext.canvas.nativeCanvas.apply {
                    val text = "PAUSED"
                    val paint = Paint()
                    paint.textSize = radius * .15f
                    paint.color = android.graphics.Color.MAGENTA

                    val textRect = Rect()
                    paint.getTextBounds(text, 0, text.length, textRect)

                    drawText(
                        text,
                        size.center.x - (textRect.width() / 2),
                        size.center.y + (textRect.width() / 2),
                        paint
                    )
                }
            }

            // Рассчитываем углы для 10 и 12 часов в радианах
            val angleForTen = ((10 * 30) - 90) * (PI / 180).toFloat()
            val angleForTwelve = ((12 * 30) - 90) * (PI / 180).toFloat()

// Рисуем сектор
            drawContext.canvas.nativeCanvas.apply {
                val paint = Paint().apply {
                    color = android.graphics.Color.argb(
                        128,
                        255,
                        0,
                        0
                    ) // Красный цвет с половинной прозрачностью
                    style = Paint.Style.FILL
                }
                val path = Path().apply {
                    moveTo(size.center.x, size.center.y) // Начинаем от центра
                    lineTo(
                        size.center.x + radius * cos(angleForTen),
                        size.center.y + radius * sin(angleForTen)
                    )
                    arcTo(
                        RectF(
                            size.center.x - radius,
                            size.center.y - radius,
                            size.center.x + radius,
                            size.center.y + radius
                        ),
                        -150f,
                        60f
                    )
                    lineTo(size.center.x, size.center.y) // Возвращаемся в центр
//                    close() // Замыкаем путь
                }
                drawPath(path, paint)
            }
        }
    }
}




/***
 * @return radians
 */
fun Int.secondsToRad(): Float {
    val angle = (360f / 60f * this) - 90f
    return (angle * (PI / 180f)).toFloat()
}

private fun getWeatherData(
    days: Int = 7,
    weatherViewModel: MainViewModel,
    context: Context
) {
    val url = "https://api.weatherapi.com/v1/forecast.json?" +
            "key=14ccc29b4b3a4938817180457240805&" +
            "q=Красноярск&" +
            "days=$days&" +
            "aqi=no&" +
            "alerts=no"

    val queue = Volley.newRequestQueue(context)

    val jsonObjectRequest = object: JsonObjectRequest(
        Method.GET,
        url,
        null,
        { response ->
            Log.d("Weather", "Response: $response")
            weatherViewModel.collectWeather(response = response)
        },
        { error ->
            Log.d("Weather", "VolleyError: $error")
        }
    ) {}
    queue.add(jsonObjectRequest)
}

@Composable
fun upRow(
    weatherCurTemp: String,
    weatherConditionIcon: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(start = 5.dp)
                .padding(vertical = 5.dp),

            ) {
            Text(
                text = "Красноярск",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(end = 5.dp)
                .padding(vertical = 5.dp),
        ) {
            Text(
                text = "+${weatherCurTemp}℃", fontSize = 24.sp
            )
            AsyncImage(
                model = weatherConditionIcon,
                contentDescription = ""
            )
        }
        Divider(modifier = Modifier.fillMaxWidth(0.8f), color = Color.Gray)
    }
}

@Composable
private fun getDayOfWeekFromDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    val dayOfWeek = date.dayOfWeek
    return dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, LocalContext.current.resources.configuration.locale).replaceFirstChar { it.uppercase() }
}