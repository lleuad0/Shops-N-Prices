package com.github.lleuad0.shopsandprices.ui.edit_product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.lleuad0.shopsandprices.Destination
import com.github.lleuad0.shopsandprices.R
import kotlinx.coroutines.launch

object EditProductDestination : Destination {
    override val route = "edit_product"
    const val argName = "id"
    val routeWArgs = "$route/{$argName}"
    val args = listOf(navArgument(argName) {
        type = NavType.LongType
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(viewModel: EditProductViewModel, productId: Long, navigate: () -> Unit) {
    val state = viewModel.stateFlow.collectAsState()
    val abstractState = viewModel.abstractStateFlow.collectAsState()
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }

    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            scope.launch { viewModel.saveProductData(name, info, state.value.shopsAndPrices) }
        }) {
            Text(text = stringResource(id = R.string.save_button))
        }
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        ) {
            TextField(value = name, onValueChange = { name = it })
            TextField(value = info, onValueChange = { info = it })

            Column {
                LazyColumn {
                    items(items = state.value.shopsAndPrices, key = {
                        state.value.shopsAndPrices.indexOf(
                            it
                        )
                    }) { item ->
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            TextField(
                                value = item.shop.name,
                                onValueChange = { scope.launch { viewModel.addNewName(it, item) } },
                                modifier = Modifier.weight(1f)
                            )
                            TextField(
                                value = item.price.toString(),
                                onValueChange = {
                                    scope.launch {
                                        viewModel.addNewPrice(
                                            it,
                                            item
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = { scope.launch { viewModel.removeItem(item) } },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = stringResource(id = R.string.del_alert_title))
                            }
                        }
                    }
                }
                Button(onClick = { scope.launch { viewModel.addItem() } }) {
                }
            }
        }
    }

    LaunchedEffect(abstractState.value.throwable) {
        abstractState.value.throwable?.let {
            // TODO: show an error
            scope.launch { viewModel.onErrorThrown() }
        }
    }
    LaunchedEffect(productId) {
        with(viewModel) {
            getProduct(productId)
            getDataForProduct(productId)
        }
    }
    LaunchedEffect(state.value.product) {
        state.value.product?.let {
            name = it.name
            info = it.additionalInfo
        }
    }
    LaunchedEffect(state.value.isSaved) {
        if (state.value.isSaved) {
            navigate()
        }
    }
}