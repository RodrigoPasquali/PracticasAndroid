package com.example.mirestaurante.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mirestaurante.domain.action.GetProducts
import com.example.mirestaurante.domain.action.SearchProducts
import com.example.mirestaurante.domain.repository.ProductRepository
import com.example.mirestaurante.infraestructure.remote.product.ProductResult
import com.example.mirestaurante.domain.model.ProductCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val repository: ProductRepository,
    private val getProducts: GetProducts,
    private val searchProducts: SearchProducts
) : ViewModel() {
    private var _productStatus = MutableLiveData<ProductsStatus>()
    var productStatus: LiveData<ProductsStatus> = _productStatus

    fun loadProducts(category: ProductCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            loading()

            onSearchProducts(category)
            onReadyProducts(category)
        }
    }

    private suspend fun onSearchProducts(category: ProductCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = searchProducts(category)
                if (response?.isSuccessful == true) {
                    saveProducts(response.body())
                } else {
                    onError()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    private suspend fun saveProducts(products: List<ProductResult>?) {
        products?.let {
            repository.saveProducts(
                it.map { productResult ->
                    productResult.mapToProduct()
                }
            )
        }
    }

    private suspend fun onReadyProducts(category: ProductCategory) {
        getProducts(category).collect{
            _productStatus.postValue(
                ProductsStatus.ReadyProducts(it)
            )
        }
    }

    private fun onError() {
        _productStatus.postValue(ProductsStatus.Error("Hubo problemas con la conexion, intente de nuevo"))
    }

    private fun loading() {
        _productStatus.postValue(ProductsStatus.Loading)
    }
}