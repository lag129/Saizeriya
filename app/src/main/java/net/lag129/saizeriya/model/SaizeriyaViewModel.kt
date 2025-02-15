package net.lag129.saizeriya.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import net.lag129.saizeriya.data.Menu
import java.io.BufferedReader
import java.io.InputStreamReader

class SaizeriyaViewModel(application: Application) : AndroidViewModel(application) {
    var selectedMenus by mutableStateOf("")
    var menuList: List<Menu> = jsonSerialize()

    private fun jsonSerialize(): List<Menu> {
        val assetManager = getApplication<Application>().assets
        val inputStream = assetManager.open("menu.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val jsonStr = bufferedReader.readText()
        val jsonArr = Json.parseToJsonElement(jsonStr).jsonArray
        val menuList = jsonArr.map {
            Json.decodeFromJsonElement<Menu>(it)
        }
        return menuList.filter {
            it.value <= 1000 && it.category != "kids"
        }
    }

    fun getResults(menuList: List<Menu>): String {
        var sumValue = 1000
        var unchangedCount = 0
        var lastSumValue = sumValue
        var selectedMenus = ""

        while (sumValue > 0 && unchangedCount < 10) {
            val randomMenu = menuList.random()

            if (randomMenu.value <= sumValue) {
                selectedMenus += Json.encodeToString(randomMenu) + ","
                sumValue -= randomMenu.value
            }

            if (lastSumValue == sumValue) {
                unchangedCount += 1
            } else {
                unchangedCount = 0
            }

            lastSumValue = sumValue
        }
        return "[${selectedMenus.dropLast(1)}]"
    }
}