package com.sagunto.saguntoappmobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OrderEntity::class], version = 1, exportSchema = false)
abstract class SaguntoDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
}