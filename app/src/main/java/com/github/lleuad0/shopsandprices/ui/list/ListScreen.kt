package com.github.lleuad0.shopsandprices.ui.list

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
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
    val snackState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.error)

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = navigateToAdding) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(
                    id = R.string.add
                )
            )
        }
    }, snackbarHost = { SnackbarHost(hostState = snackState) }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
            snackState.showSnackbar(it.localizedMessage ?: errorMessage)
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
            items.refresh()
            scope.launch { viewModel.onDeleted() }
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
        modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) })
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.product_name), fontStyle = FontStyle.Italic)
            Text(text = item.name)
        }
    }
}