package com.sagunto.saguntoappmobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SyncStatus {
    PENDING,
    SYNCED
}

@Entity(tableName = "orders_queue")
data class OrderEntity(

    @PrimaryKey val id: String,
    val isPaid: Boolean,
    val userId: Int,
    val customerId: Int?,


    val linesPayload: String,

    val syncStatus: String = SyncStatus.PENDING.name,
    val createdAt: Long = System.currentTimeMillis()
)