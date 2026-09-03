package com.example.githubdemo.viewmodel.market


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.githubdemo.supabase.ProductRepository
import com.example.githubdemo.model.market.Product

import kotlinx.coroutines.launch



class MarketViewModel: ViewModel(){



    private val repository =
        ProductRepository()



    val products =
        mutableStateOf<List<Product>>(
            emptyList()
        )




    init{

        loadProducts()

    }





    fun loadProducts(){


        viewModelScope.launch{


            try{


                products.value =
                    repository.getProducts()



            }catch(e:Exception){


                products.value =
                    emptyList()


            }


        }


    }






    // Recommendation for Cart

    fun getRecommendation(

        cartProductIds:Set<String>

    ):List<Product>{



        return products.value

            .filter { product ->



                product.id !in cartProductIds



            }

            .take(4)



    }




}