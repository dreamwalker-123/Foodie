package com.example.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Category
import com.example.model.Product
import com.example.ui.components.BottomBarButton
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
//    val tags = viewModel.tagsUiState.collectAsStateWithLifecycle()
    val categories = viewModel.categoriesUiState.collectAsStateWithLifecycle()
    val products = viewModel.productsUiState.collectAsStateWithLifecycle()
    val currentCategory = viewModel.currentCategory.value

    Column {
        // Topline на фигне
        CatalogTopAppBar(
            onFilterClick = { /*TODO*/ },
            onSearchClick = { /*TODO*/ },
            )
        // Лист с категориями
        ListItemCategories(
            uiState = categories.value,
            currentCategory = currentCategory,
            onCategoryClick = viewModel::updateCurrentCategory,
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.categories_chips_padding_top))
        )
        // Сетка с продуктами
        ItemList(
            uiState = products.value,
            currentCategory = currentCategory,
            columns = columns,
            onCardClick = onProductClick,
            onAddClick = viewModel::addProductInCart,
            onRemoveClick = viewModel::removeProductFromCart,
            )
        // нижняя кнопка с суммой
        CartButton(
            uiState = products.value,
            onClick = onCartClick,
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.bottom_app_bar_margin_horizontal),
                    vertical = dimensionResource(id = R.dimen.bottom_app_bar_margin_vertical)
                )
        )
    }
}
@Composable
fun CatalogTopAppBar(
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = stringResource(R.string.filter_button_description)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription ="logo",
            modifier = Modifier
                .size(70.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onSearchClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = stringResource(R.string.search_button_description)
            )
        }
    }
}

@Composable
private fun CartButton(
    uiState: ProductsUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState is ProductsUiState.Success) {
        val sumCart = uiState.product.map { it.key.price_current * it.value }.sum()
        if (sumCart > 0) {
            BottomBarButton(
                onClick = onClick,
                modifier = modifier
            ) {
                Icon(
                    painter = painterResource(id = com.example.ui.R.drawable.ic_cart),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.icon_cart_on_button_padding_end))
                        .size(dimensionResource(id = R.dimen.icon_cart_on_button_size))
                )
                Text(text = sumCart.formatAsPriceString())
            }
        }
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
                uiState.product.filterKeys { it.category_id == category.id }
            } ?: uiState.product
            if (products.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = columns,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    items(products.keys.toList()) { product ->
                        ProductCard(
                            product = product,
                            quantityInCart = products.getOrDefault(product, 0),
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
fun ListItemCategories(
    uiState: CategoriesUiState,
    currentCategory: Category?,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    when(uiState) {
        CategoriesUiState.Loading -> { /*TODO*/
        }

        CategoriesUiState.Error -> { /*TODO*/
        }

        CategoriesUiState.Empty -> { /*TODO*/
        }

        is CategoriesUiState.Success -> {
            val categoriesList = uiState.categories
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_between_chips)),
                modifier = modifier.padding(bottom = 8.dp)
            ) {
                itemsIndexed(categoriesList) { index, category ->
                    // FIXME: Убрать обводку
                    FilterChip(
                        selected = category == currentCategory,
                        onClick = { onCategoryClick(category) },
                        label = {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            labelColor = MaterialTheme.colorScheme.onSurface,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.surface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = false,
                            selected = false,
                            borderColor = Color.Transparent,
                            borderWidth = dimensionResource(id = com.example.ui.R.dimen.zero)
                        ),
                        elevation = FilterChipDefaults.filterChipElevation(
                            elevation = dimensionResource(
                                id = com.example.ui.R.dimen.zero
                            )
                        ),
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.chip_height))
                            .padding(
                                start = if (index == 0) dimensionResource(id = R.dimen.start_chip_padding_start)
                                else dimensionResource(id = com.example.ui.R.dimen.zero)
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCatalogScreen() {
//    CatalogScreen()
}