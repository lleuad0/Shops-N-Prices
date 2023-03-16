package com.github.lleuad0.shopsandprices.data

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.room.util.useCursor
import com.github.lleuad0.shopsandprices.data.dao.ProductDao
import com.github.lleuad0.shopsandprices.data.entities.ProductDb
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class TestProductDao {
    private lateinit var productDao: ProductDao
    private lateinit var db: Database

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication().applicationContext,
            Database::class.java
        ).allowMainThreadQueries().build()
        productDao = db.productDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun insertNoDao(productDb: ProductDb): Long {
        db.openHelper.writableDatabase.insert(
            "products",
            CONFLICT_IGNORE,
            ContentValues().apply {
                put("product_name", productDb.productName)
                put("product_info", productDb.productInfo)
            })
        var id: Long = -1
        db.query(
            query = "SELECT * FROM products WHERE product_name = :arg1 AND product_info = :arg2",
            args = arrayOf(productDb.productName, productDb.productInfo)
        ).useCursor {
            it.moveToFirst()
            val idIndex = it.getColumnIndex("product_id")
            id = it.getLong(idIndex)
        }
        assertNotEquals(-1, id)
        return id
    }

    @Test
    fun testInsert() {
        val query = "SELECT * from products"
        db.query(query = query, args = null).useCursor {
            assertEquals(0, it.count)
        }

        val id = productDao.insertProduct(PRODUCT)
        assertNotEquals(-1, id)
        db.query(query = query, args = null).useCursor {
            assertEquals(1, it.count)
            it.moveToFirst()

            val nameIndex = it.getColumnIndex("product_name")
            val infoIndex = it.getColumnIndex("product_info")
            assertEquals(PRODUCT.productName, it.getString(nameIndex))
            assertEquals(PRODUCT.productInfo, it.getString(infoIndex))
        }
    }

    @Test
    fun testUpdate() {
        val id = insertNoDao(PRODUCT)
        val updatedProduct = ProductDb("updated name", "updated info", id)
        productDao.updateProduct(updatedProduct)

        val query =
            "SELECT * FROM products WHERE product_name = :arg1 AND product_info = :arg2 AND product_id = :arg3"
        db.query(query = query, args = arrayOf(PRODUCT.productName, PRODUCT.productInfo, id))
            .useCursor {
                assertEquals(0, it.count)
            }
        db.query(
            query = query,
            args = arrayOf(updatedProduct.productName, updatedProduct.productInfo, id)
        ).useCursor {
            assertEquals(1, it.count)
        }
    }

    @Test
    fun testGetById() {
        val id = insertNoDao(PRODUCT)
        assertEquals(
            ProductDb(PRODUCT.productName, PRODUCT.productInfo, id),
            productDao.getProductById(id)
        )
    }

    @Test
    fun testGetByInvalid() {
        assertNull(productDao.getProductById(-1))
    }

    @Test
    fun testDelete() {
        val id = insertNoDao(PRODUCT)
        productDao.deleteProduct(ProductDb(PRODUCT.productName, PRODUCT.productInfo, id))
        db.query(
            query = "SELECT * FROM products WHERE product_name = :arg1 AND product_info = :arg2 AND product_id = :arg3",
            args = arrayOf(PRODUCT.productName, PRODUCT.productInfo, id)
        )
            .useCursor {
                assertEquals(0, it.count)
            }
    }

    @Test
    fun testDeleteInvalid() {
        productDao.deleteProduct(ProductDb(PRODUCT.productName, PRODUCT.productInfo, -1))
    }

    @Test
    fun testSelectAll() {
        val ids = PRODUCTS.map { insertNoDao(it) }
        runTest {
            val products = productDao.selectAll().load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = Int.MAX_VALUE,
                    placeholdersEnabled = false
                )
            )
            val data = (products as? PagingSource.LoadResult.Page)?.data?.toTypedArray()
            data?.forEachIndexed { index, productDb ->
                val inserted =
                    ProductDb(PRODUCTS[index].productName, PRODUCTS[index].productInfo, ids[index])
                assertEquals(inserted, productDb)
            }
        }
    }

    @Test
    fun testSelectAllZero() {
        runTest {
            val products = productDao.selectAll().load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = Int.MAX_VALUE,
                    placeholdersEnabled = false
                )
            )
            val data = (products as? PagingSource.LoadResult.Page)?.data?.toTypedArray()
            assertArrayEquals(emptyArray(), data)
        }
    }

    private companion object {
        val PRODUCT = ProductDb("test name", "test info")
        val PRODUCTS = arrayOf(
            ProductDb("name 1", "info 1"),
            ProductDb("name 2", "info 2"),
            ProductDb("name 3", "")
        )
    }
}