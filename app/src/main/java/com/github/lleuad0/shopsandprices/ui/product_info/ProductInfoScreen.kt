package com.github.lleuad0.shopsandprices.ui.product_info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.lleuad0.shopsandprices.Destination
import com.github.lleuad0.shopsandprices.R

object ProductInfoDestination : Destination {
    override val route = "product_info"
    const val argName = "id"
    val routeWArgs = "$route/{$argName}"
    val args = listOf(navArgument(argName) {
        type = NavType.LongType
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfoScreen(viewModel: ProductInfoViewModel, productId: Long, toEdit: (Long) -> Unit) {
    val state = viewModel.stateFlow.collectAsState()

    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { toEdit(productId) }) {
            Text(text = stringResource(id = R.string.edit_activity_title))
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        ) {
            Text(text = state.value.product?.name ?: "")
            Text(text = state.value.product?.additionalInfo ?: "")
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(text = stringResource(id = R.string.info_table_shops_title))
                Text(text = stringResource(id = R.string.info_table_prices_title))
            }
            LazyColumn(Modifier.fillMaxSize()) {
                items(items = state.value.shopsAndPrices, key = { it.shop.id }) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text(text = it.shop.name, modifier = Modifier.weight(1f))
                        Text(text = it.price.toString(), modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

    LaunchedEffect(productId) {
        with(viewModel) {
            getProduct(productId)
            getDataForProduct(productId)
        }
    }
}