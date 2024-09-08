package com.example.saizeriya.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.saizeriya.data.Menu
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class SaizeriyaViewModel(application: Application): AndroidViewModel(application) {

    var selectedMenus by mutableStateOf("")
    var menuList: List<Menu> = jsonSerialize()

    private fun jsonSerialize(): List<Menu> {
        val assetManager = getApplication<Application>().assets
        val inputStream = assetManager.open("menu.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val jsonStr = bufferedReader.readText()
        val jsonArr = JSONArray(jsonStr)
        val menuList = List(jsonArr.length()) { index ->
            Json.decodeFromString<Menu>(jsonArr[index].toString())
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
                unchangedCount+=1
            } else {
                unchangedCount = 0
            }

            lastSumValue = sumValue
        }
        return "[${selectedMenus.dropLast(1)}]"
    }
}