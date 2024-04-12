package com.example.pizzaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pizzaapp.ui.theme.PizzaAppTheme
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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TextFieldDefaults
import androidx.navigation.NavHostController

@Composable
fun SecondPage(navController: NavHostController) {
    Box(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#C9E1F2")))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            topSecondPageText()
            carouselSecondPage()
            timeIntervalSelectorSecondPage()
            descriptionField()
            Row {
                Box() {
                    SaveButton(navController)
                }
            }
        }
    }
}

@Composable
private fun descriptionField() {
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Описание",
            style = MaterialTheme.typography.bodyLarge
        )

    }
    TransparentPlaceholderTextField(
        placeholder = "Введите текст...",
        onValueChange = { /* Обработка изменений введенного текста */ }
    )
}

@Composable
private fun timeIntervalSelectorSecondPage() {
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Выберите время",
            style = MaterialTheme.typography.bodyLarge
        )

    }
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .padding(15.dp)
            .fillMaxWidth(0.9f)
            .background(Color(android.graphics.Color.parseColor("#F1F5F9"))),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val columnPadding = 20.dp
        val colTextStyleMine = TextStyle(fontSize = 30.sp)

        Column(
            modifier = Modifier.padding(columnPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = "C",
                    style = colTextStyleMine,
                    color = Color.LightGray
                )
            }
            Row {
                Text(text = "12:00", style = colTextStyleMine)
            }
        }
        Column(
            modifier = Modifier.padding(columnPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(text = ">", style = colTextStyleMine)
        }
        Column(
            modifier = Modifier.padding(columnPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = "До",
                    style = colTextStyleMine,
                    color = Color.LightGray
                )
            }
            Row {
                Text(text = "14:00", style = colTextStyleMine)
            }
        }
    }
}


@Composable
private fun carouselSecondPage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        var value = 22
        val boxHeight = 80.dp
        val boxPadding = 25.dp
        val numbersMap = mapOf(
            "1" to "Пн",
            "2" to "Вт",
            "3" to "Ср",
            "4" to "Чт",
            "5" to "Пт",
            "6" to "Сб",
            "7" to "Вс"
        )
        for (i in 1..3) {
            if (value == 23) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor("#8572FF")))
                        .padding(boxPadding)
                        .height(boxHeight),
                ) {
                    Column {
                        Text(
                            text = value.toString(), color = Color.White
                        )
                        numbersMap[i.toString()]?.let {
                            Text(
                                text = it, color = Color.White
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(15.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor("#F1F5F9")))
                        .padding(boxPadding)
                        .height(boxHeight)
                ) {
                    Column {
                        Text(
                            text = value.toString()
                        )
                        numbersMap[i.toString()]?.let {
                            Text(
                                text = it
                            )
                        }
                    }
                }
            }
            value++
        }
        Box(
            modifier = Modifier
                .padding(15.dp)
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor("#F1F5F9")))
                .padding(boxPadding)
                .height(boxHeight)
        ) {
            Column {
                Text(
                    text = "Другая", fontSize = 14.sp
                )

                Text(
                    text = "Дата", fontSize = 10.sp
                )

            }
        }
    }
}

@Composable
private fun topSecondPageText() {
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Давайте выберем занятие",
            style = MaterialTheme.typography.headlineLarge
        )
    }
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = "Выберите дату", style = MaterialTheme.typography.bodyLarge)

    }
}

@Composable
//    fun SideSheetView(navController: NavHostController) {
fun SideSheetView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#F3EDF7")))
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        // Возврат на предыдущий экран (назад)
//                            navController.navigateUp()
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
                Text(
                    text = "New note",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            }
            TransparentPlaceholderTextField(
                placeholder = "Введите текст...",
                onValueChange = { /* Обработка изменений введенного текста */ }
            )
        }
    }
}

@Composable
fun TransparentPlaceholderTextField(
    placeholder: String,
    onValueChange: (TextFieldValue) -> Unit
) {
    val backgroundColor = Color(android.graphics.Color.parseColor("#F3EDF7"))
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { PlaceholderText(text = placeholder) },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .fillMaxWidth(0.9f),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            focusedIndicatorColor = backgroundColor,
            disabledIndicatorColor = backgroundColor,
            unfocusedIndicatorColor = backgroundColor
        ),
        textStyle = TextStyle(color = Color.Black)
    )

}

@Composable
private fun PlaceholderText(text: String) {
    Text(
        text = text,
        color = Color(android.graphics.Color.parseColor("#BCA1BE")),
        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Normal)
    )
}

@Composable
fun SaveButton(navController: NavHostController) {
    Button(
        onClick = {
// Возврат на предыдущий экран (назад)
            navController.navigate("MainPage")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Небольшой отступ с обеих сторон
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)) // Малиновый цвет
    ) {
        Text("Сохранить", color = Color.White) // Текст белого цвета
    }
}
