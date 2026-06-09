package com.sagunto.saguntoappmobile.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.CreateOrderRequest
import com.sagunto.saguntoappmobile.data.network.dto.createOrder.OrderLineRequest
import com.sagunto.saguntoappmobile.data.network.dto.getProductsByCustomerId.GetProductsByCustomerId
import com.sagunto.saguntoappmobile.domain.interfaces.IOrderRepository
import com.sagunto.saguntoappmobile.domain.interfaces.IProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddOrderViewModel(
   private val orderRepository: IOrderRepository,
   private val productRepository: IProductRepository,
   private val customerId: Int
): ViewModel() {

    val isSaguntino : String =  if (customerId == 1) "Precio Saguntino" else "Precio Cliente"
    private val _isLoading = MutableStateFlow(false)
    private val _showCodeDialog = MutableStateFlow(false)
    val showCodeDialog: StateFlow<Boolean> = _showCodeDialog.asStateFlow()
    private val _messageDialog = MutableStateFlow("")
    val messageDialog: StateFlow<String> = _messageDialog.asStateFlow()


    private val _cart = MutableStateFlow<List<OrderLineRequest>>(emptyList())
    val cart: StateFlow<List<OrderLineRequest>> = _cart.asStateFlow()

    private val _productCatalog = MutableStateFlow<List<GetProductsByCustomerId>>(emptyList())
    val productCatalog: StateFlow<List<GetProductsByCustomerId>> = _productCatalog.asStateFlow()



    init { //Como en angular, para la ejecución en el momento de su creación
        getProducts()
    }

    fun getProducts(){
        viewModelScope.launch{
            val result = productRepository.getProductsByCustomerId(customerId)

            result.fold(
                onSuccess = { listaProductos ->
                    _productCatalog.value = listaProductos
                },
                onFailure = { error ->
                    //TODO
                }
            )
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

    fun saveOrder(){
        viewModelScope.launch {
            _isLoading.value = true

            val order = CreateOrderRequest(
                isPaid = false,
                userId = 1,//TODO aquí tengo qeu sacar el usuario logado
                customerId = customerId,
                products = cart.value
            )

            val result = orderRepository.addOrder(order)

            result.fold(
                onSuccess = {
                    _messageDialog.value = "Pedido creado con exito!"
                    _showCodeDialog.value = true
                },
                onFailure = {
                    _messageDialog.value = "El pedido no se ha podido crear, por favor vuelve a intentarlo"
                    _showCodeDialog.value = true
                }
            )

            _isLoading.value = false
        }
    }

    fun dismissDialog() {
        _showCodeDialog.value = false
        _messageDialog.value = ""
    }
}