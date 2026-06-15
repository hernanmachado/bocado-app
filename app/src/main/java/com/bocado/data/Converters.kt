package com.bocado.data

import androidx.room.TypeConverter
import com.bocado.model.OrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromOrderItemList(value: List<OrderItem>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toOrderItemList(value: String): List<OrderItem> {
        val listType = object : TypeToken<List<OrderItem>>() {}.type
        return Gson().fromJson(value, listType) ?: emptyList()
    }
}
