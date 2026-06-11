package com.sagunto.saguntoappmobile.data.network.dto.searchUsers

sealed class SearchUsersResponse {
    data class SingleResult(val user: UserResponse) : SearchUsersResponse()
    data class MultipleResults(val users: List<UserResponse>) : SearchUsersResponse()
    data class Error(val message: String) : SearchUsersResponse()
}