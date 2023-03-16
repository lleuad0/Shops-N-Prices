package com.github.lleuad0.shopsandprices.ui.new_product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.lleuad0.shopsandprices.Destination
import com.github.lleuad0.shopsandprices.R
import kotlinx.coroutines.launch

object NewPriceDestination : Destination {
    override val route = "new_price"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPriceScreen(
    parentViewModel: NewProductNavViewModel,
    viewModel: NewPriceViewModel,
    navigateNext: () -> Unit,
) {
    val parentState = parentViewModel.stateFlow.collectAsState()
    val abstractParentState = parentViewModel.abstractStateFlow.collectAsState()
    val state = viewModel.stateFlow.collectAsState()
    val abstractState = viewModel.abstractStateFlow.collectAsState()
    val scope = rememberCoroutineScope()
    var price by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        ) {
            Text(text = parentState.value.product?.name ?: "")
            Text(text = parentState.value.shop?.name ?: "")
            TextField(
                value = price,
                onValueChange = { price = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(Modifier.fillMaxWidth(), Arrangement.End) {
                Button(onClick = {
                    scope.launch {
                        viewModel.addPrice(
                            parentState.value.product!!,
                            parentState.value.shop!!,
                            price.toDouble()
                        )
                    }
                }) {
                    Text(text = stringResource(id = R.string.save_button))
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
    LaunchedEffect(abstractParentState.value.throwable) {
        abstractParentState.value.throwable?.let {
            // TODO: show an error
            scope.launch { parentViewModel.onErrorThrown() }
        }
    }
    LaunchedEffect(state.value.isPriceAdded) {
        if (state.value.isPriceAdded) {
            navigateNext()
        }
    }
}