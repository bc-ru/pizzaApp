package com.example.pizzaapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pizzaapp.data.MainDB
import com.example.pizzaapp.data.MainDB_Impl
import com.example.pizzaapp.data.NameEntity
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import kotlin.math.roundToInt

class MainViewModel(val database: MainDB) : ViewModel() {
    val itemsList = database.dao.getAllItems()
    val newText = mutableStateOf("")
    var nameEntity: NameEntity? = null

    val weatherList: MutableState<List<Weather>> = mutableStateOf(listOf())
    val weatherSelected = mutableIntStateOf(0)

    fun insertItem() = viewModelScope.launch {
        val nameItem = nameEntity?.copy(text = newText.value)
            ?: NameEntity(text =  newText.value)
        database.dao.insertItem(nameItem)
        nameEntity = null
        newText.value = ""
    }

    fun deleteItem(item: NameEntity) = viewModelScope.launch {
        database.dao.deleteItem(item)
    }

    fun updateItemText(item: NameEntity) = viewModelScope.launch {
        database.dao.updateItem(item)
    }

    fun collectWeather(response: JSONObject) {
        var list = ArrayList<Weather>()

        val days = response.getJSONObject("forecast").getJSONArray("forecastday")

        for (i in 0 until days.length()) {
            val item = days[i] as JSONObject

            list.add(
                Weather(
                    curTemp = item.getJSONObject("day").getString("avgtemp_c").toDouble().roundToInt().toString(),
                    day = item.getString("date"),
                    conditionIcon = "https:${item.getJSONObject("day").getJSONObject("condition").getString("icon")}",
                )
            )
        }

        weatherList.value = list
        println("fetched")
    }

    companion object{
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as DbInit).database
                return MainViewModel(database) as T
            }
        }
    }
}


