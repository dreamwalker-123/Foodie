package com.example.basket

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.catalog.R
import com.example.model.Product
import com.example.ui.components.BottomBarButton
import com.example.ui.components.Counter
import com.example.ui.components.EmptyScreen
import com.example.ui.utils.formatAsPriceString
import androidx.compose.foundation.lazy.items

@Composable
fun BasketRoute(
    onUpClick: () -> Unit,
    viewModel: BasketViewModel = hiltViewModel()
) {
    val cart by viewModel.cartFlow.collectAsStateWithLifecycle()
    BasketScreen(
        cart = cart,
        onAddProductClick = viewModel::addProductInCart,
        onRemoveProductClick = viewModel::removeProductFromCart,
        onUpClick = onUpClick,
    )
}
@Composable
private fun BasketScreen(
    cart: Map<Product, Int>,
    onAddProductClick: (Product) -> Unit,
    onRemoveProductClick: (Product) -> Unit,
    onUpClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            BasketTopAppBar(onUpClick = onUpClick)
        },
        bottomBar = {
            if (cart.isNotEmpty()) {
                BottomBarButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = com.example.ui.R.dimen.bottom_app_bar_margin_horizontal),
                            vertical = dimensionResource(id = com.example.ui.R.dimen.bottom_app_bar_margin_vertical)
                        )
                ) {
                    val cartSum =
                        cart.map { it.key.price_current * it.value }.sum().formatAsPriceString()
                    Text(text = stringResource(R.string.button_order_text, cartSum))
                }
            }
        },
    ) { innerPadding ->
        if (cart.isNotEmpty()) {
            ProductColumn(
                cart = cart,
                onAddProductClick = onAddProductClick,
                onRemoveProductClick = onRemoveProductClick,
                contentPadding = innerPadding
            )
        } else {
            EmptyScreen(message = stringResource(R.string.empty_screen_message))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasketTopAppBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.top_app_bar_title))
        },
        navigationIcon = {
            IconButton(onClick = onUpClick) {
                //FIXME: изменить цвет стрелки на оранжевый, как в фигме
                Icon(
                    painter = painterResource(id = com.example.ui.R.drawable.arrowleft),
                    contentDescription = stringResource(com.example.ui.R.string.button_up_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(navigationIconContentColor = MaterialTheme.colorScheme.primary),
        modifier = modifier.shadow(elevation = dimensionResource(id = com.example.ui.R.dimen.app_bars_elevation))
    )
}

@Composable
private fun ProductColumn(
    cart: Map<Product, Int>,
    onAddProductClick: (Product) -> Unit,
    onRemoveProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(dimensionResource(id = com.example.ui.R.dimen.zero))
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(cart.toList()) { (product, quantity) ->
            ProductItem(
                product = product,
                quantity = quantity,
                onAddProductClick = onAddProductClick,
                onRemoveProductClick = onRemoveProductClick
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun ProductItem(
    product: Product,
    quantity: Int,
    onAddProductClick: (Product) -> Unit,
    onRemoveProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = product.name,
                color = MaterialTheme.colorScheme.onSurface,
                minLines = 2,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        supportingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_after_counter)),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = dimensionResource(id = R.dimen.footer_bottom_padding))
            ) {
                Counter(
                    amount = quantity,
                    onMinusClick = { onRemoveProductClick(product) },
                    onPlusClick = { onAddProductClick(product) },
                    elevated = false,
                    modifier = Modifier.weight(1f)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.price_column_width))
                        .height(dimensionResource(id = R.dimen.price_column_height))
                ) {
                    Text(
                        text = product.price_current.formatAsPriceString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                    product.price_old?.let {
                        Text(
                            text = it.formatAsPriceString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        leadingContent = {
            Image(
                painter = painterResource(id = com.example.ui.R.drawable.soup),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.leading_image_padding_vertical))
                    .size(dimensionResource(id = R.dimen.leading_image_size))
            )
        },
        modifier = modifier.height(dimensionResource(id = R.dimen.list_item_height))
    )
}

@Preview(showBackground = true)
@Composable
private fun CartScreenPreview() {
    BasketScreen(
        cart = mapOf(
            Product(
                name = "Том Ям",
                price_current = 48000,
                price_old = 56000
            ) to 1,
            Product(
                name = "Бургер с кучей овощей и чеддером",
                price_current = 48000
            ) to 1,
            Product(
                name = "Кусок пиццы с соусом песто и оливками",
                price_current = 48000
            ) to 1,
            Product(
                name = "Ролл Сяки Маки",
                price_current = 48000
            ) to 1
        ),
        onAddProductClick = {},
        onRemoveProductClick = {},
        onUpClick = {}
    )
}
