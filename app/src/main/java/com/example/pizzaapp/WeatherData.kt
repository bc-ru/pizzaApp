package com.example.pizzaapp

import com.google.gson.JsonParser
import java.text.SimpleDateFormat
import java.util.*

/**
 * Класс, хранящий информацию о прогнозе и обрабатывающий её
 *
 * @property date время, на сервере во время обработки запроса
 * @property temperature текущая температура в градусах Цельсия
 * @property icon имя jpg файла погоды на сервере без разширения
 * @property condition текущая погода в виде kebab-case  английского значения
 * @property conditionValue текущая погода в виде строки на русском
 * @property iconURL полный путь к изображению погоды на сервере
 */
data class WeatherData(
    var date: Date,
    var temperature: Int,
    var icon: String,
    var condition: String,
) {
    companion object {
        var conditions: Map<String, String> = mapOf(
            "clear" to "ясно",
            "partly-cloudy" to "малооблачно",
            "cloudy" to "облачно с прояснениями",
            "overcast" to "пасмурно",
            "drizzle" to "морось",
            "light-rain" to "небольшой дождь",
            "rain" to "дождь",
            "moderate-rain" to "умеренно сильный дождь",
            "heavy-rain" to "сильный дождь",
            "continuous-heavy-rain" to "длительный сильный дождь",
            "showers" to "ливень",
            "wet-snow" to "дождь со снегом",
            "light-snow" to "небольшой снег",
            "snow" to "снег",
            "snow-showers" to "снегопад",
            "hail" to "град",
            "thunderstorm" to "гроза",
            "thunderstorm-with-rain" to "дождь с грозой",
            "thunderstorm-with-hail" to "гроза с градом",
        )

        fun conditionToPrettyString(condition: String): String {
            return conditions[condition].toString()
        }

        fun getIconUrl(icon: String): String {
            return "https://yastatic.net/weather/i/icons/funky/dark/$icon.svg"
        }

        fun parseFromString(data: String): WeatherData {
            with(JsonParser.parseString(data).asJsonObject) {
                return try {
                    WeatherData(
                        SimpleDateFormat("yyyy-MM-dd", Locale.US)
                            .parse(getAsJsonArray("forecasts")[0]
                                .asJsonObject.get("date").asString) ?: Date(),
                        getAsJsonObject("fact").get("temp").asInt,
                        getAsJsonObject("fact").get("icon").asString,
                        getAsJsonObject("fact").get("condition").asString
                    )
                } catch (e: Exception) {
                    WeatherData(Date(0), 0, e.toString(), "")
                }
            }

        }
    }

    val conditionValue get() = conditionToPrettyString(condition)
    val iconURL get() = getIconUrl(icon)
    val Date.toPrettyString get() = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(this)
}