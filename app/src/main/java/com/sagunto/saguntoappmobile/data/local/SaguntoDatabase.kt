package com.sagunto.saguntoappmobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [OrderEntity::class, ProductEntity::class, UserEntity::class],
    version = 5,
    exportSchema = false
)
abstract class SaguntoDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
}