package com.github.lleuad0.shopsandprices.ui.list

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.github.lleuad0.shopsandprices.Destination
import com.github.lleuad0.shopsandprices.R
import com.github.lleuad0.shopsandprices.domain.model.Product
import kotlinx.coroutines.launch

object ListDestination : Destination {
    override val route = "list"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel,
    navigateToAdding: () -> Unit,
    navigateToInfo: (Long) -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.stateFlow.collectAsState()
    val abstractState = viewModel.abstractStateFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val items = state.value.products.collectAsLazyPagingItems()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = navigateToAdding) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(
                    id = R.string.add
                )
            )
        }
    }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(items = items, key = { it.id }) {
                    it?.let {
                        ProductView(
                            item = it,
                            onClick = { navigateToInfo(it.id) },
                            onLongClick = { viewModel.deleteProduct(it) })
                    }
                }
            }
            if (items.itemSnapshotList.isEmpty() && items.loadState.prepend.endOfPaginationReached && items.loadState.append.endOfPaginationReached) {
                Text(
                    text = stringResource(id = R.string.no_items),
                    Modifier.align(Alignment.Center)
                )
            }
        }
    }

    LaunchedEffect(abstractState.value.throwable) {
        abstractState.value.throwable?.let {
            // TODO: show an error
            scope.launch { viewModel.onErrorThrown() }
        }
    }
    LaunchedEffect(state.value.isProductDeleted) {
        if (state.value.isProductDeleted) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.deleted_success),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.onDeleted()
        }
    }
    LaunchedEffect(null) {
        scope.launch { viewModel.getAllData() }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductView(
    modifier: Modifier = Modifier,
    item: Product,
    onClick: (Product) -> Unit,
    onLongClick: (Product) -> Unit,
) {
    Card(
        modifier.combinedClickable(
            onClick = { onClick(item) },
            onLongClick = { onLongClick(item) })
    ) {
        Text(text = stringResource(id = R.string.product_name_title))
        Text(text = item.name)
    }
}