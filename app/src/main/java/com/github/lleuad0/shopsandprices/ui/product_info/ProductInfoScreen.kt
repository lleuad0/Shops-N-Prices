package com.github.lleuad0.shopsandprices.ui.product_info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.lleuad0.shopsandprices.Destination
import com.github.lleuad0.shopsandprices.R
import kotlinx.coroutines.launch

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
    val abstractState = viewModel.abstractStateFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.error)

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { toEdit(productId) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(
                    id = R.string.edit_product
                )
            )
        }
    }, snackbarHost = { SnackbarHost(hostState = snackState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.value.product?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            state.value.product?.additionalInfo?.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.product_info),
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(text = it)
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = if (state.value.product?.additionalInfo?.isNotEmpty() == true) 16.dp else 0.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.info_table_shops_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(0.5f), textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.info_table_prices_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(items = state.value.shopsAndPrices, key = { it.shop.id }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = it.shop.name,
                            modifier = Modifier.fillMaxWidth(0.5f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = it.price.toString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }


    LaunchedEffect(abstractState.value.throwable) {
        abstractState.value.throwable?.let {
            snackState.showSnackbar(it.localizedMessage ?: errorMessage)
            scope.launch { viewModel.onErrorThrown() }
        }
    }
    LaunchedEffect(productId) {
        with(viewModel) {
            getProduct(productId)
            getDataForProduct(productId)
        }
    }
}