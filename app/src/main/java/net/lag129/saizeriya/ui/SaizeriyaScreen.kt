package net.lag129.saizeriya.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.json.Json
import net.lag129.saizeriya.data.Menu
import net.lag129.saizeriya.model.SaizeriyaViewModel
import net.lag129.saizeriya.ui.theme.SaizeriyaTheme

@Composable
fun SaizeriyaScreen(
    modifier: Modifier = Modifier,
    viewModel: SaizeriyaViewModel = viewModel()
) {
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
        DisplayResult(
            jsonArr = uiState,
            modifier = Modifier
        )
        Button(onClick = { viewModel.selectedMenus = viewModel.getResults(viewModel.menuList) }) {
            Text("ガチャを引く")
        }
    }
}

@Composable
fun DisplayResult(
    jsonArr: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (jsonArr.isBlank()) {
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "ボタンを押してね！！"
            )
            Spacer(modifier = Modifier.padding(4.dp))
            HorizontalDivider()
        } else {
            val selectedList = Json.decodeFromString<List<Menu>>(jsonArr)
            Spacer(modifier = Modifier.padding(4.dp))
            selectedList.forEach { menu ->
                MenuCard(menu)
            }
            Text(
                text = "合計 ${selectedList.sumOf { it.value }} 円"
            )
            Spacer(modifier = Modifier.padding(4.dp))
            HorizontalDivider()
        }
    }
}

@Composable
fun MenuCard(
    menu: Menu,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp)
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
}

@Preview
@Composable
private fun PreviewUiScreen() {
    SaizeriyaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MenuCard(
                menu = Menu(
                    id = 1202,
                    name = "小エビのサラダ",
                    value = 350,
                    category = "salad"
                ),
                modifier = Modifier
            )
        }
    }
}