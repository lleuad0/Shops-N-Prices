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

object NewProductDestination : Destination {
    override val route = "new_product"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductScreen(parentViewModel: NewProductNavViewModel, navigateNext: () -> Unit) {
    val state = parentViewModel.stateFlow.collectAsState()
    val abstractState = parentViewModel.abstractStateFlow.collectAsState()
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.product_name))
            TextField(value = name, onValueChange = { name = it }, label = {
                Text(
                    text = stringResource(
                        id = R.string.product_name_hint
                    )
                )
            })
            Text(text = stringResource(id = R.string.product_info))
            TextField(value = info, onValueChange = { info = it }, label = {
                Text(
                    text = stringResource(
                        id = R.string.product_info_hint
                    )
                )
            })
            Row(Modifier.fillMaxWidth(), Arrangement.End) {
                Button(onClick = { scope.launch { parentViewModel.addProduct(name, info) } }) {
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
    LaunchedEffect(state.value.isProductAdded) {
        if (state.value.isProductAdded) {
            navigateNext()
            scope.launch { parentViewModel.onRedirectedProduct() }
        }
    }
}