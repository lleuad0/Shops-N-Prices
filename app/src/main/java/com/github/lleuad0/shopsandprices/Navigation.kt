package com.github.lleuad0.shopsandprices

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.lleuad0.shopsandprices.ui.edit_product.EditProductDestination
import com.github.lleuad0.shopsandprices.ui.edit_product.EditProductScreen
import com.github.lleuad0.shopsandprices.ui.list.ListDestination
import com.github.lleuad0.shopsandprices.ui.list.ListScreen
import com.github.lleuad0.shopsandprices.ui.new_product.*
import com.github.lleuad0.shopsandprices.ui.product_info.ProductInfoDestination
import com.github.lleuad0.shopsandprices.ui.product_info.ProductInfoScreen

interface Destination {
    val route: String
}

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ListDestination.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ListDestination.route) {
            ListScreen(viewModel = hiltViewModel(),
                navigateToAdding = { navController.navigate(NewProductDestination.route) },
                navigateToInfo = { navController.navigate("${ProductInfoDestination.route}/$it") })
        }
        composable(ProductInfoDestination.routeWArgs, ProductInfoDestination.args) {
            ProductInfoScreen(
                viewModel = hiltViewModel(),
                productId = it.arguments?.getLong(ProductInfoDestination.argName) ?: -1,
                toEdit = { id -> navController.navigate("${EditProductDestination.route}/$id") }
            )
        }
        composable(EditProductDestination.routeWArgs, EditProductDestination.args) {
            EditProductScreen(
                viewModel = hiltViewModel(),
                productId = it.arguments?.getLong(EditProductDestination.argName) ?: -1,
                navigate = { navController.navigate(ListDestination.route) { popUpTo(ListDestination.route) } }
            )
        }
        navigation(NewProductDestination.route, NewProductNavigation.route) {
            composable(NewProductDestination.route) {
                val parent = remember(it) {
                    navController.getBackStackEntry(NewProductNavigation.route)
                }
                val parentModel = hiltViewModel<NewProductNavViewModel>(parent)
                NewProductScreen(
                    parentViewModel = parentModel,
                    navigateNext = { navController.navigate(NewShopDestination.route) })
            }
            composable(NewShopDestination.route) {
                val parent = remember(it) {
                    navController.getBackStackEntry(NewProductNavigation.route)
                }
                val parentModel = hiltViewModel<NewProductNavViewModel>(parent)
                NewShopScreen(
                    parentViewModel = parentModel,
                    navigateNext = { navController.navigate(NewPriceDestination.route) })
            }
            composable(NewPriceDestination.route) {
                val parent = remember(it) {
                    navController.getBackStackEntry(NewProductNavigation.route)
                }
                val parentModel = hiltViewModel<NewProductNavViewModel>(parent)
                NewPriceScreen(parentViewModel = parentModel,
                    viewModel = hiltViewModel(),
                    navigateNext = {
                        navController.navigate(ListDestination.route) {
                            popUpTo(
                                NewProductNavigation.route
                            )
                        }
                    })
            }
        }
    }
}

object NewProductNavigation : Destination {
    override val route = "add_new_product"
}
