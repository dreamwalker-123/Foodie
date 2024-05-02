package com.example.catalog

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodie_api.model.Categories
import com.example.foodie_api.model.CategoriesItem
import com.example.foodie_api.model.Products

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsState()
    Column {
        // Topline на фигне
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)) {
            Image(
                painter = painterResource(R.drawable.filter),
                contentDescription ="filter_button",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription ="logo",
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.search),
                contentDescription ="search_button",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        // ListItem Categories
        ListItemCategories(
            Categories(listOf(
                CategoriesItem(1, "Новинка"),
                CategoriesItem(2, "Вегетарианское блюдо"),
                CategoriesItem(3, "Хит!"),
                CategoriesItem(4, "Острое"),
                CategoriesItem(5, "Экспресс-меню")
            ))
        )
//        ItemList(state.value.products)
        TODO()
    }
}

@Composable
fun ItemList(products: Products) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(products.list) {
            TODO()
        }
    }
}

@Composable
fun ListItemCategories(categories: Categories) {
    var state by remember { mutableIntStateOf(0) } // вывести выше

    LazyRow {
        items(categories.list) {
            var isSelected = false
            if (categories.list[state] == it)
                isSelected = true
            val backColor: Color by animateColorAsState(targetValue = if (isSelected) Color(0xFFF15412) else Color.Transparent)
            val textColor: Color by animateColorAsState(targetValue = if (isSelected) Color.White else Color.Black)

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        state = categories.list.indexOf(it)
                    }
            ) {
                Text(text = it.name, color = textColor,
                    modifier = Modifier
                        .background(backColor)
                        .padding(10.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewCatalogScreen() {
    CatalogScreen()
}