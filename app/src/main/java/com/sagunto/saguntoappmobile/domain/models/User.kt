package com.sagunto.saguntoappmobile.domain.models

data class User (
    val id: Int = -1,
    val name: String,
    val surname: String,
    val saguntinoCode: String = "",
    val rolId: Int = 2 //Rol Id para usuario sin permisos, por defecto se crean todos con este nivel
)