package com.example.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Category
import com.example.model.Product
import com.example.model.Tag
import com.example.ui.components.BottomBarButton
import com.example.ui.components.Counter
import com.example.ui.components.EmptyScreen
import com.example.ui.components.ErrorScreen
import com.example.ui.components.LoadingScreen
import com.example.ui.utils.formatAsPriceString

@Composable
fun CatalogRoute(
    columns: GridCells,
    onProductClick: (Int) -> Unit,
    onBasketClick: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val products by viewModel.productsUiState.collectAsStateWithLifecycle()
    val categories by viewModel.categoriesUiState.collectAsStateWithLifecycle()
    val currentCategory = viewModel.currentCategory.value
    val tags by viewModel.tagsUiState.collectAsStateWithLifecycle()
    val checkedState by viewModel.mapWithCheckedState.collectAsStateWithLifecycle()

    CatalogScreen(
        products = products,
        categories = categories,
        currentCategory = currentCategory,
        tags = tags,
        checkedState = checkedState,
        columns = columns,
        onBasketClick = onBasketClick,
        onCategoryClick = viewModel::updateCurrentCategory,
        onProductClick = onProductClick,
        onAddProductClick = viewModel::addProductInCart,
        onRemoveProductClick = viewModel::removeProductFromCart,
        onTagClicked = viewModel::addOrRemoveTagId,
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    products: ProductsUiState,
    categories: CategoriesUiState,
    currentCategory: Category?,
    tags: TagUiState,
    checkedState: Map<Int, Boolean>,
    columns: GridCells,
    onBasketClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onProductClick: (Int) -> Unit,
    onAddProductClick: (Product) -> Unit,
    onRemoveProductClick: (Product) -> Unit,
    onTagClicked: (Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isVisibleSearchScreen by remember { mutableStateOf(false) }
    var textFromSearch by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // Topline на фигне
            CatalogTopAppBar(
                onFilterClick = { showBottomSheet = true },
                onSearchClick = { isVisibleSearchScreen = true },
                numberOfTags = checkedState.values.count { it },
                isVisibleSearchScreen = isVisibleSearchScreen,
                onUpClick = { isVisibleSearchScreen = false },
                enteredText = { textFromSearch = it },
                textFromSearch = textFromSearch,
                onTheClearFieldClick = { textFromSearch = "" }
            )
        },
        bottomBar = {
            BasketButton(
                uiState = products,
                onClick = onBasketClick,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.bottom_app_bar_margin_horizontal),
                        vertical = dimensionResource(id = R.dimen.bottom_app_bar_margin_vertical)
                    )
            )
        },
    ) { innerPadding ->
        // FIXME: вывести в отдельную composable функцию
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp,)) {
                    Text(text = "Подобрать блюда",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.align(Alignment.Start))
                    LazyColumn(Modifier.padding(top = 10.dp)) {
                        if (tags is TagUiState.Success) {

                            itemsIndexed(tags.tags) {index, tag ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = tag.name)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Checkbox(
                                        checked = checkedState[tag.id] ?: false,
                                        onCheckedChange = {
                                            onTagClicked(tag.id)
                                        }
                                    )
                                }
                                if (index < tags.tags.size - 1) {
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                    Button(onClick = { showBottomSheet = false },
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFFF15412),
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 40.dp)) {
                        Text("Готово")
                    }
                }
            }
        }

        val density = LocalDensity.current
        Column(modifier = Modifier.padding(innerPadding)) {
            // Лист с категориями
            AnimatedVisibility(
                visible = !isVisibleSearchScreen,
                enter = slideInVertically {
                    with(density) { -40.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically { with(density) { -40.dp.roundToPx() }
                } + shrinkVertically() + fadeOut(targetAlpha = 0.3f)
            ) {
                Column {
                    ListItemCategories(
                        uiState = categories,
                        currentCategory = currentCategory,
                        onCategoryClick = onCategoryClick,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = R.dimen.categories_chips_padding_top))
                    )
                }
            }

            // Сетка с продуктами
            ItemList(
                uiState = products,
                currentCategory = currentCategory,
                checkedState = checkedState,
                columns = columns,
                isVisibleSearchScreen = isVisibleSearchScreen,
                textFromSearch = textFromSearch,
                onCardClick = onProductClick,
                onAddClick = onAddProductClick,
                onRemoveClick = onRemoveProductClick,
            )
        }
    }
}


@Composable
fun CatalogTopAppBar(
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
    numberOfTags: Int,
    isVisibleSearchScreen: Boolean,
    enteredText: (String) -> Unit,
    onUpClick: () -> Unit,
    textFromSearch: String,
    onTheClearFieldClick: () -> Unit,
) {
    // FIXME: можно настроить более интересную анимацию, а не просто дефолтную FadeIn FadeOut
    // FIXME: специально настроил замедленную анимацию, чтобы было заметно ее наличие
    Crossfade(targetState = isVisibleSearchScreen,
        label = "Crossfade of CatalogTopAppBar",
        animationSpec = tween(durationMillis = 500),
    ) { screen ->
        when(screen) {
            true -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onUpClick) {
                        //FIXME: изменить цвет стрелки на оранжевый, как в фигме
                        Icon(
                            painter = painterResource(id = com.example.ui.R.drawable.arrowleft),
                            contentDescription = stringResource(com.example.ui.R.string.button_up_description)
                        )
                    }

                    TextField(
                        value = textFromSearch,
                        onValueChange = enteredText,
                        maxLines = 2,
                        placeholder = { Text("Найти блюдо") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )

                    if (textFromSearch != "") {
                        IconButton(onClick = onTheClearFieldClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.cancel),
                                contentDescription = stringResource(com.example.ui.R.string.button_up_description)
                            )
                        }
                    }
                }
            }
            false-> {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    val textMeasurer = rememberTextMeasurer()
                    IconButton(
                        onClick = onFilterClick,
                        modifier = Modifier
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = stringResource(R.string.filter_button_description)
                        )
                        if (numberOfTags > 0) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                translate(left = 23f, top = -23f) {
                                    drawCircle(Color(0xFFF15412), radius = 7.dp.toPx())
                                }
                                translate(left = 70f, top = 14f) {
                                    drawText(
                                        textMeasurer,
                                        "$numberOfTags",
                                        style = TextStyle(fontSize = 11.sp, color = Color.White)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "logo",
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
        }
    }
}

@Composable
fun BasketButton(
    uiState: ProductsUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState is ProductsUiState.Success) {
        val sumCart = uiState.product.map { it.key.price_current * it.value }.sum()
        if (sumCart > 0) {
            BottomBarButton(
                onClick = onClick,
                modifier = modifier,
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
    checkedState: Map<Int, Boolean>,
    columns: GridCells,
    isVisibleSearchScreen: Boolean,
    textFromSearch: String,
    onCardClick: (Int) -> Unit,
    onAddClick: (Product) -> Unit,
    onRemoveClick: (Product) -> Unit,
) {
    when(uiState) {
        is ProductsUiState.Loading -> {
            LoadingScreen()
        }

        is ProductsUiState.Error -> { ErrorScreen(message = uiState.error.message ?: "") }
        ProductsUiState.Empty -> {
            EmptyScreen(message = stringResource(R.string.empty_screen_message))
        }
        is ProductsUiState.Success -> {
            var products = uiState.product
            if (isVisibleSearchScreen) {
                if (textFromSearch == "") {
                    products = mapOf()
                } else {
                    products = products.filterKeys {
                        it.name.lowercase().contains(textFromSearch.lowercase())
                    }
                }
            } else {
                products = currentCategory?.let { category ->
                    uiState.product.filterKeys {
                        it.category_id == category.id
                    }
                } ?: uiState.product

                if (checkedState.values.contains(true)) {
                    products = products.filterKeys {
                        if (it.tag_ids.isEmpty()) {
                            false
                        } else it.tag_ids.any { item -> checkedState[item]!! }
                    }
                }
            }

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
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clickable { onClick(product.id) }
    ) {
        Box {
            Image(
                painter = painterResource(id = com.example.ui.R.drawable.soup),
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
                            disabledBorderColor = Color.Transparent,
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
    CatalogScreen(
        products = ProductsUiState.Success(
            mapOf(
                Product(
                    name = "Том Ям",
                    category_id = 1,
                    price_current = 72000,
                    price_old = 80000,
                    measure = 500,
                    measure_unit = "г",
                    tag_ids = listOf(1)
                ) to 2,
                *List(5) {
                    Product(
                        name = "Название блюда $it",
                        category_id = 1,
                        price_current = 48000,
                        measure = 500,
                        measure_unit = "г",
                        tag_ids = listOf(2)
                    ) to 0
                }.toTypedArray()
            )
        ),
        categories = CategoriesUiState.Success(
            listOf(
                Category(id = 1, name = "Роллы"),
                Category(name = "Суши"),
                Category(name = "Наборы"),
                Category(name = "Горячие блюда"),
                Category(name = "Супы"),
                Category(name = "Десерты"),
            )
        ),
        tags = TagUiState.Success(
            listOf(
                Tag(1, "Новинка"),
                Tag(2, "Вегетарианское блюдо"),
                Tag(3, "Хит!"),
                Tag(4, "Острое"),
                Tag(5, "Экспресс-меню"),
            )
        ),
        checkedState = mapOf(1 to true, 2 to true, 3 to false, 4 to false, 5 to false),
        currentCategory = null,//Category(id = 1, name = "Роллы"),
        columns = GridCells.Fixed(2),
        onAddProductClick = {},
        onRemoveProductClick = {},
        onCategoryClick = {},
        onProductClick = {},
        onBasketClick = {},
        onTagClicked = {},
    )
}