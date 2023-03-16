package com.github.lleuad0.shopsandprices.ui.new_product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.lleuad0.shopsandprices.Destination
import com.github.lleuad0.shopsandprices.R
import kotlinx.coroutines.launch

object NewShopDestination : Destination {
    override val route = "new_shop"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewShopScreen(parentViewModel: NewProductNavViewModel, navigateNext: () -> Unit) {
    val state = parentViewModel.stateFlow.collectAsState()
    val abstractState = parentViewModel.abstractStateFlow.collectAsState()
    val scope = rememberCoroutineScope()
    var shopName by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.product_shop_title))
            TextField(value = shopName, onValueChange = { shopName = it })
            Row(Modifier.fillMaxWidth(), Arrangement.End) {
                Button(onClick = { scope.launch { parentViewModel.addShop(shopName, "") } }) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }


    LaunchedEffect(abstractState.value.throwable) {
        abstractState.value.throwable?.let {
            // TODO: show an error
            scope.launch { parentViewModel.onErrorThrown() }
        }
    }
    LaunchedEffect(state.value.isShopAdded) {
        if (state.value.isShopAdded) {
            navigateNext()
            scope.launch { parentViewModel.onRedirectedShop() }
        }
    }
}