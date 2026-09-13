package com.sagunto.saguntoappmobile.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.CreateOrderRequest
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.OrderLineRequest
import com.sagunto.saguntoappmobile.data.network.dto.getProductsByCustomerId.GetProductsByCustomerId
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.SearchUsersResponse
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.UserResponse
import com.sagunto.saguntoappmobile.data.interfaces.IOrderRepository
import com.sagunto.saguntoappmobile.data.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.data.managers.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddOrderViewModel(
    private val orderRepository: IOrderRepository,
    private val productRepository: IProductRepository,
    private val userRepository: IUserRepository,
    val isSaguntino: Boolean,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showResultDialog = MutableStateFlow(false)
    val showResultDialog: StateFlow<Boolean> = _showResultDialog.asStateFlow()

    private val _isOrderSuccess = MutableStateFlow<Boolean?>(null)
    val isOrderSuccess: StateFlow<Boolean?> = _isOrderSuccess.asStateFlow()

    private val _messageDialog = MutableStateFlow("")
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()

    private val _cart = MutableStateFlow<List<OrderLineRequest>>(emptyList())
    val cart: StateFlow<List<OrderLineRequest>> = _cart.asStateFlow()

    private val _productCatalog = MutableStateFlow<List<GetProductsByCustomerId>>(emptyList())
    val productCatalog: StateFlow<List<GetProductsByCustomerId>> = _productCatalog.asStateFlow()

    private val _showSearchDialog = MutableStateFlow(false)
    val showSearchDialog: StateFlow<Boolean> = _showSearchDialog.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<UserResponse>>(emptyList())
    val searchResults: StateFlow<List<UserResponse>> = _searchResults.asStateFlow()

    val isSaguntinoCodeValid: Boolean = true
    val isSaguntinoCodeTouched: Boolean = false

    init {
        observeLocalProducts()
        refreshProductsSilently()
    }

    private fun observeLocalProducts() {
        viewModelScope.launch {
            // 🛠️ Nos suscribimos al Flow de Room. Cada vez que SQLite cambie, esto se re-ejecuta solo.
            productRepository.getAllProductsLocal().collect { localEntities ->
                val mappedProducts = localEntities.map { entity ->
                    // Reutilizamos tu DTO actual para no romper la UI ni el carrito
                    GetProductsByCustomerId(
                        id = entity.id,
                        name = entity.name,
                        // 🛠️ Mapeo dinámico: el ViewModel decide qué precio ver
                        price = if (isSaguntino) entity.priceMember else entity.priceGuest
                    )
                }
                _productCatalog.value = mappedProducts
            }
        }
    }

    // 🛠️ --- Lógica de búsqueda ---
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun showSearchDialog() {
        _showSearchDialog.value = true
    }

    fun dismissSearchDialog() {
        _showSearchDialog.value = false
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun searchUsers() {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = userRepository.searchUsers(_searchQuery.value)) {
                is SearchUsersResponse.MultipleResults -> {
                    if (result.users.isEmpty()) {
                        _messageDialog.value = "No se encontraron saguntinos con ese código o nombre."
                        _isOrderSuccess.value = false
                        _showResultDialog.value = true
                    } else {
                        _searchResults.value = result.users
                        _showSearchDialog.value = true
                    }
                }
                is SearchUsersResponse.SingleResult -> {
                    _searchResults.value = listOf(result.user)
                    _showSearchDialog.value = true
                }
                is SearchUsersResponse.Error -> {
                    _messageDialog.value = result.message
                    _isOrderSuccess.value = false
                    _showResultDialog.value = true
                }
            }

            _isLoading.value = false
        }
    }

    fun addProductToCart(product: GetProductsByCustomerId) {
        _cart.update { currentCart ->
            val newList = currentCart.toMutableList()
            val existingItemIndex = newList.indexOfFirst { it.productId == product.id }

            if (existingItemIndex != -1) {
                val existingItem = newList[existingItemIndex]
                newList[existingItemIndex] = existingItem.copy(
                    quantity = existingItem.quantity + 1
                )
            } else {
                newList.add(
                    OrderLineRequest(
                        productId = product.id,
                        quantity = 1,
                        priceSnapshot = product.price,
                        name = product.name
                    )
                )
            }
            newList
        }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        _cart.update { currentCart ->
            val newList = currentCart.toMutableList()
            val index = newList.indexOfFirst { it.productId == productId }
            val existingItem = newList[index]
            newList[index] = existingItem.copy(
                quantity = existingItem.quantity + newQuantity
            )
            newList
        }
    }

    fun deleteProductCart(productId: Int) {
        _cart.update { currentCart ->
            val newList = currentCart.toMutableList()
            newList.removeIf { it.productId == productId }
            newList
        }
    }

    fun saveOrder(isPaid: Boolean, targetCustomerId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true

            val finalCustomerId = when {
                !isSaguntino -> -1
                isPaid -> -2
                targetCustomerId != null -> targetCustomerId
                else -> -1
            }

            val order = CreateOrderRequest(
                isPaid = isPaid,
                userId = sessionManager.currentUser.value?.id ?: -1,
                customerId = finalCustomerId,
                products = cart.value
            )

            val result = orderRepository.addOrder(order)

            result.fold(
                onSuccess = {
                    _messageDialog.value = "¡Pedido creado con éxito!"
                    _isOrderSuccess.value = true
                    _showResultDialog.value = true
                },
                onFailure = {
                    _messageDialog.value = "El pedido no se ha podido crear, por favor vuelve a intentarlo"
                    _isOrderSuccess.value = false
                    _showResultDialog.value = true
                }
            )

            _isLoading.value = false
        }
    }

    fun dismissResultDialog() {
        _showResultDialog.value = false
        _messageDialog.value = ""
        _isOrderSuccess.value = null
    }

    fun refreshProductsSilently() {
        viewModelScope.launch {
            try {
                productRepository.syncProducts()
            } catch (e: Exception) {
                // Falla silencioso: si no hay red o da timeout, no molestamos al camarero
                // porque ya está viendo los productos cacheados en Room
                Log.w("ADD_ORDER_VM", "Sincronización silenciosa omitida (sin red o error de API)")
            }
        }
    }
}