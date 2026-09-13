package com.sagunto.saguntoappmobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments_queue")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val customerId: Int,
    val orderIdsPayload: String, // Guardamos la lista de UUIDs en formato CSV o JSON (ej: "uuid1,uuid2")
    val syncStatus: String = SyncStatus.PENDING.name,
    val createdAt: Long = System.currentTimeMillis()
)