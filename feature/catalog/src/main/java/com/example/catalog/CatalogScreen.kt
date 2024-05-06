package com.example.catalog

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodie_api.model.Category
import com.example.foodie_api.model.Product
import com.example.ui.components.Counter
import com.example.ui.components.EmptyScreen
import com.example.ui.components.ErrorScreen
import com.example.ui.components.LoadingScreen
import com.example.ui.utils.formatAsPriceString

@Composable
fun CatalogScreen(
    columns: GridCells,
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val tags = viewModel.tagsUiState.collectAsState()
    val categories = viewModel.categoriesUiState.collectAsState()
    val products = viewModel.productsUiState.collectAsState()
    val currentCategory = viewModel.currentCategory.value

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
        // Лист с категориями
        ListItemCategories(
            listOf(
                Category(1, "Новинка"),
                Category(2, "Вегетарианское блюдо"),
                Category(3, "Хит!"),
                Category(4, "Острое"),
                Category(5, "Экспресс-меню")
            )
        )
        // Сетка с продуктами
        ItemList(
            uiState = products.value,
            currentCategory = currentCategory,
            columns = GridCells.Fixed(2),
            onCardClick = onProductClick,
            onAddClick = viewModel::addProductInCart,
            onRemoveClick = viewModel::removeProductFromCart,
            )
    }
}

@Composable
fun ItemList(
    uiState: ProductsUiState,
    currentCategory: Category?,
    columns: GridCells,
    onCardClick: (Int) -> Unit,
    onAddClick: (Product) -> Unit,
    onRemoveClick: (Product) -> Unit,
) {
    when(uiState) {
        ProductsUiState.Loading -> {
            LoadingScreen()
        }

        is ProductsUiState.Error -> { ErrorScreen(message = uiState.error.message ?: "") }
        ProductsUiState.Empty -> {
            EmptyScreen(message = stringResource(R.string.empty_screen_message))
        }
        is ProductsUiState.Success -> {
            val products = currentCategory?.let { category ->
                uiState.product.filter { it.category_id == category.id }
            } ?: uiState.product
            if (products.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = columns,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            quantityInCart = productsMap.getOrDefault(product, 0),
                            onClick = onCardClick,
                            onAddClick = onAddClick,
                            onRemoveClick = onRemoveClick
                        )
                    }
                }
            } else {
                EmptyScreen(message = stringResource(R.string.empty_filters_screen_message))
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    quantityInCart: Int,
    onClick: (Int) -> Unit,
    onAddClick: (Product) -> Unit,
    onRemoveClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clickable { onClick(product.id) }
    ) {
        Box {
            Image(
                painter = painterResource(id = com.example.ui.R.drawable.photo),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.image_padding_bottom))
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_between_tag_items)),
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.tags_row_padding),
                        top = dimensionResource(id = R.dimen.tags_row_padding)
                    )
            ) {
                product.price_old?.let {
                    Image(
                        painter = painterResource(id = com.example.ui.R.drawable.ic_discount),
                        contentDescription = null
                    )
                }
                if (product.tag_ids.contains(2)) {
                    Image(
                        painter = painterResource(id = com.example.ui.R.drawable.ic_without_meat),
                        contentDescription = null
                    )
                }
                if (product.tag_ids.contains(4)) {
                    Image(
                        painter = painterResource(id = com.example.ui.R.drawable.ic_spicy),
                        contentDescription = null
                    )
                }
            }
        }
        Text(
            text = product.name,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.product_name_padding_horizontal))
                .padding(bottom = dimensionResource(id = R.dimen.product_name_padding_bottom))
        )
        Text(
            text = "${product.measure} ${product.measure_unit}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.product_name_padding_horizontal))
        )
        if (quantityInCart == 0) {
            ElevatedButton(
                onClick = {
                    onAddClick(product)
                },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = dimensionResource(id = R.dimen.elevation_price_button)
                ),
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_price_button))
                    .fillMaxWidth()
            ) {
                Text(
                    text = product.price_current.formatAsPriceString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                product.price_old?.let { priceOld ->
                    Text(
                        text = priceOld.formatAsPriceString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = TextDecoration.LineThrough,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.price_old_padding))
                    )
                }
            }
        } else {
            Counter(
                amount = quantityInCart,
                onMinusClick = { onRemoveClick(product) },
                onPlusClick = { onAddClick(product) },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.counter_padding))
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun ListItemCategories(categories: List<Category>) {
    var state by remember { mutableIntStateOf(0) } // вывести выше

    LazyRow {
        items(categories) {
            var isSelected = false
            if (categories[state] == it)
                isSelected = true
            val backColor: Color by animateColorAsState(targetValue = if (isSelected) Color(0xFFF15412) else Color.Transparent)
            val textColor: Color by animateColorAsState(targetValue = if (isSelected) Color.White else Color.Black)

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        state = categories.indexOf(it)
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