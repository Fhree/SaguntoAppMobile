package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.SearchUsersResponse
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.UserResponse
import com.sagunto.saguntoappmobile.data.network.dto.unpaidOrder.UnpaidOrderResponse
import com.sagunto.saguntoappmobile.domain.interfaces.IOrderRepository
import com.sagunto.saguntoappmobile.domain.interfaces.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UnpaidOrderViewModel(
    private val orderRepository: IOrderRepository,
    private val userRepository: IUserRepository
): ViewModel() {

    private val _unpaidOrders = MutableStateFlow<List<UnpaidOrderResponse>>(emptyList())
    val unpaidOrders: StateFlow<List<UnpaidOrderResponse>> = _unpaidOrders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showResultDialog = MutableStateFlow(false)
    val showResultDialog: StateFlow<Boolean> = _showResultDialog.asStateFlow()

    private val _messageDialog = MutableStateFlow("")
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()

    private val _unpaidOrdersTotalPrice = MutableStateFlow(0.0)
    val unpaidOrdersTotalPrice: StateFlow<Double> = _unpaidOrdersTotalPrice.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _customer = MutableStateFlow<UserResponse?>(null)
    val customer: StateFlow<UserResponse?> = _customer.asStateFlow()

    private val _isPaymentSuccess = MutableStateFlow<Boolean?>(null)
    val isPaymentSuccess: StateFlow<Boolean?> = _isPaymentSuccess.asStateFlow()

    private val _searchResults = MutableStateFlow<List<UserResponse>>(emptyList())
    val searchResults: StateFlow<List<UserResponse>> = _searchResults.asStateFlow()

    private val _showSearchDialog = MutableStateFlow(false)
    val showSearchDialog: StateFlow<Boolean> = _showSearchDialog.asStateFlow()

    fun executeSearch() {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = userRepository.searchUsers(_searchQuery.value)) {
                is SearchUsersResponse.SingleResult -> {
                    selectCustomer(result.user)
                }
                is SearchUsersResponse.MultipleResults -> {
                    _searchResults.value = result.users
                    _showSearchDialog.value = true
                }
                is SearchUsersResponse.Error -> {
                    _messageDialog.value = result.message
                    _showResultDialog.value = true
                }
            }
            _isLoading.value = false
        }
    }

     fun loadOrders(customerId: Int){
        viewModelScope.launch {
            _isLoading.value = true

            val result = orderRepository.getUnpaidOrders(customerId)

            result.fold(
                onSuccess = { orderList ->
                    _unpaidOrders.value = orderList
                    _unpaidOrdersTotalPrice.value = orderList.sumOf { it.total }
                },
                onFailure = { error ->
                    _messageDialog.value = "Error al cargar los pedidos"
                    _showResultDialog.value = true
                }
            )

            _isLoading.value = false
        }
    }

    fun selectCustomer(user: UserResponse) {
        _customer.value = user
        _showSearchDialog.value = false
        _searchQuery.value = ""
        loadOrders(user.id)
    }

    fun processPayment(){
        val currentCustomer = _customer.value ?: return

        viewModelScope.launch {
            _isLoading.value = true

            val result = orderRepository.payOrders(currentCustomer.id)

            result.fold(
                onSuccess = {
                    _unpaidOrders.value = emptyList()
                    _unpaidOrdersTotalPrice.value = 0.0
                    _isPaymentSuccess.value = true
                    _messageDialog.value = "Deuda liquidada correctamente"
                    _showResultDialog.value = true
                },
                onFailure = { error ->
                    _isPaymentSuccess.value = false
                    _messageDialog.value = "Error al pagar los pedidos en el servidor"
                    _showResultDialog.value = true
                }
            )

            _isLoading.value = false
        }
    }

    fun dismissResultDialog(){
        _showResultDialog.value = false
        _messageDialog.value = ""
    }

    fun dismissSearchDialog(){
        _showSearchDialog.value = false
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}