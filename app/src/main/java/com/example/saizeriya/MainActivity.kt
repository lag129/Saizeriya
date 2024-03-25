package com.example.saizeriya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.saizeriya.ui.theme.SaizeriyaTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    private var menuList: List<Menu> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuList = jsonSerialize()
        setContent {
            SaizeriyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val jsonArr = calc(menuList)
                        Text(
                            text = "サイゼリヤN円ガチャ",
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Divider()
                        DisplayResult(jsonArr)
//                        DisplayButton(this@MainActivity, selectedList.value.toMutableList())
                    }
                }
            }
        }
    }

    private fun jsonSerialize(): List<Menu> {
        val assetManager = this.resources.assets
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

    private fun calc(menuList: List<Menu>): String {
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
        println("[${selectedMenus.dropLast(1)}]")
        return "[${selectedMenus.dropLast(1)}]"
    }
}

@Composable
fun DisplayResult(
    jsonArr : String) {
    val selectedList = Json.decodeFromString<List<Menu>>(jsonArr)
    if (selectedList.isEmpty()) {
        Text(
            text = "No selection yet",
        )
    } else {
        for (menu in selectedList) {
            println(menu.name)
            CardList(menu)
        }
    }
    Spacer(modifier = Modifier.padding(4.dp))
    Text("合計 ${selectedList.sumOf { it.value }} 円")
    Spacer(modifier = Modifier.padding(4.dp))
    Divider()
}

@Composable
fun CardList(menu: Menu) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        val menuId = menu.id.toString()
        val menuValue = menu.value.toString()
        Text(
            text = menu.name,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "$menuId, $menuValue 円",
            modifier = Modifier.fillMaxWidth()
        )
    }
    Divider()
}
