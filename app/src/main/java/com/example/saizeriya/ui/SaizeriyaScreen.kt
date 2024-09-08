package com.example.saizeriya.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.saizeriya.data.Menu
import com.example.saizeriya.ui.theme.SaizeriyaTheme
import kotlinx.serialization.json.Json

@Composable
fun SaizeriyaScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: SaizeriyaViewModel = viewModel()
    val uiState = viewModel.selectedMenus

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "サイゼリヤ1000円ガチャ",
            modifier = Modifier
        )
        HorizontalDivider()
        DisplayResult(uiState)
        Button(onClick = { viewModel.selectedMenus = viewModel.getResults(viewModel.menuList) }) {
            Text("ガチャを引く")
        }
    }
}

@Composable
fun DisplayResult(jsonArr : String) {
    if (jsonArr.isBlank()) {
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "ボタンを押してね！！",
        )
        Spacer(modifier = Modifier.padding(4.dp))
        HorizontalDivider()
    } else {
        val selectedList = Json.decodeFromString<List<Menu>>(jsonArr)
        selectedList.forEach { menu ->
            CardList(menu)
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text("合計 ${selectedList.sumOf { it.value }} 円")
        Spacer(modifier = Modifier.padding(4.dp))
        HorizontalDivider()
    }
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
    HorizontalDivider()
}

@Preview
@Composable
fun PreviewUiScreen() {
    SaizeriyaTheme {
        SaizeriyaScreen()
    }
}