package com.example.data

import com.example.data.utils.asInternalModel
import com.example.model.Category
import com.example.model.Product
import com.example.model.Tag
import com.example.network.model.NetworkCategory
import com.example.network.model.NetworkProduct
import com.example.network.model.NetworkTag
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MappersUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun networkProductMappers_isCorrect() {
        val networkProduct = NetworkProduct(
            carbohydrates_per_100_grams = 10.0,
            category_id = 10,
            description = "Парам парам",
            energy_per_100_grams = 10.0,
            fats_per_100_grams = 10.0,
            id = 10,
            image = "Парам парам",
            measure = 10,
            measure_unit = "Парам парам",
            name = "Парам парам",
            price_current = 10,
            price_old = null,
            proteins_per_100_grams = 10.0,
            tag_ids = listOf(),
        )
        val product = Product(
            carbohydrates_per_100_grams = 10.0,
            category_id = 10,
            description = "Парам парам",
            energy_per_100_grams = 10.0,
            fats_per_100_grams = 10.0,
            id = 10,
            image = "Парам парам",
            measure = 10,
            measure_unit = "Парам парам",
            name = "Парам парам",
            price_current = 10,
            price_old = null,
            proteins_per_100_grams = 10.0,
            tag_ids = listOf(),
        )
        assertEquals(networkProduct.asInternalModel(), product)
    }

    @Test
    fun networkCategoryMappers_isCorrect() {
        val networkCategory = NetworkCategory(id = 10, name = "Парам пам пам")
        val category = Category(id = 10, name = "Парам пам пам")
        assertEquals(networkCategory.asInternalModel(), category)
    }
    @Test
    fun networkTagMappers_isCorrect() {
        val networkTag = NetworkTag(id = 10, name = "Парам пам пам")
        val tag = Tag(id = 10, name = "Парам пам пам")
        assertEquals(networkTag.asInternalModel(), tag)
    }
}