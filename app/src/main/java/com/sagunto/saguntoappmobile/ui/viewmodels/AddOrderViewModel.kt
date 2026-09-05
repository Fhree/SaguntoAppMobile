package com.sagunto.saguntoappmobile.ui.viewmodels

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
    private val userRepository: IUserRepository, // 🛠️ Inyectamos el repositorio de usuarios
    val isSaguntino: Boolean, // 🛠️ Recibimos el booleano desde la navegación
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
        getProducts()
    }

    fun getProducts(){
        viewModelScope.launch{
            // 🛠️ Pasamos el booleano al repositorio de productos
            val result = productRepository.getProductsByCustomerId(isSaguntino)

            result.fold(
                onSuccess = { productList ->
                    _productCatalog.value = productList
                },
                onFailure = { error ->
                    //TODO: Manejar el error de carga
                }
            )
        }
    }

    // 🛠️ --- INJERTO: Lógica de búsqueda ---
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
        val currentQuery = _searchQuery.value.trim()
        if (currentQuery.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true

            when (val result = userRepository.searchUsers(currentQuery)) {
                is SearchUsersResponse.SingleResult -> {
                    dismissSearchDialog()
                    // Si encuentra uno solo, guardamos directamente "A deber" con su ID
                    saveOrder(isPaid = false, targetCustomerId = result.user.id)
                }
                is SearchUsersResponse.MultipleResults -> {
                    _searchResults.value = result.users
                }
                is SearchUsersResponse.Error -> {
                    _messageDialog.value = result.message
                    _searchResults.value = emptyList()
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

    // 🛠️ MODIFICADO: Aceptamos un ID específico opcional si se buscó a un saguntino
    fun saveOrder(isPaid: Boolean, targetCustomerId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true

            // 🛠️ Árbol de decisión para aislar el Magic Number (-2)
            val finalCustomerId = when {
                !isSaguntino -> -1             // Invitado
                isPaid -> -2                   // Saguntino pagado en barra
                targetCustomerId != null -> targetCustomerId // Saguntino moroso encontrado
                else -> -1                     // Fallback de seguridad
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
}