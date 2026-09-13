package com.sagunto.saguntoappmobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
@JvmSuppressWildcards
interface UserDao {

    @Query("""
        SELECT * FROM users 
        WHERE LOWER(saguntinoCode) = LOWER(:query) 
           OR normalizedSearch LIKE '%' || LOWER(:query) || '%' 
        ORDER BY name ASC
    """)
    suspend fun searchUsersLocal(query: String): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>): List<Long>

    @Query("DELETE FROM users")
    suspend fun clearAll(): Int
}