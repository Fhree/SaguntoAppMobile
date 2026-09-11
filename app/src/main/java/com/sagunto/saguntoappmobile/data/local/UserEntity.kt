package com.sagunto.saguntoappmobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val surname: String,
    val saguntinoCode: String,
    val normalizedSearch: String
)