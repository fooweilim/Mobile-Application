package com.example.githubdemo.viewmodel.market


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.githubdemo.supabase.CartRepository
import com.example.githubdemo.model.market.CartProduct

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch



class CartViewModel : ViewModel(){



    private val repository =
        CartRepository()





    private val _cartProducts =
        MutableStateFlow<List<CartProduct>>(
            emptyList()
        )



    val cartProducts: StateFlow<List<CartProduct>> =
        _cartProducts





    init{

        loadCart()

    }







    fun loadCart(){


        viewModelScope.launch{


            try{


                _cartProducts.value =
                    repository.getCartProducts()



            }catch(e:Exception){


                e.printStackTrace()


                _cartProducts.value =
                    emptyList()


            }


        }


    }








    fun addToCart(

        productId:String

    ){


        viewModelScope.launch{


            try{


                repository.addCart(

                    productId

                )


                // refresh cart count immediately

                loadCart()



            }catch(e:Exception){


                e.printStackTrace()


            }



        }


    }








    fun increase(

        item:CartProduct

    ){



        viewModelScope.launch{


            try{


                repository.updateQuantity(

                    item.cartId,

                    item.quantity + 1

                )


                loadCart()



            }catch(e:Exception){


                e.printStackTrace()


            }



        }



    }








    fun decrease(

        item:CartProduct

    ){



        viewModelScope.launch{


            try{


                if(item.quantity > 1){



                    repository.updateQuantity(

                        item.cartId,

                        item.quantity - 1

                    )


                }



                loadCart()



            }catch(e:Exception){


                e.printStackTrace()


            }



        }



    }








    fun delete(

        item:CartProduct

    ){



        viewModelScope.launch{


            try{


                repository.deleteCart(

                    item.cartId

                )


                loadCart()



            }catch(e:Exception){


                e.printStackTrace()


            }



        }



    }









    // Delete multiple selected products

    fun deleteSelected(

        items:List<CartProduct>

    ){



        viewModelScope.launch{


            try{


                items.forEach{ item ->



                    repository.deleteCart(

                        item.cartId

                    )



                }




                loadCart()



            }catch(e:Exception){


                e.printStackTrace()


            }



        }



    }








    fun total():Double{


        return _cartProducts.value.sumOf {


            it.product.price *
                    it.quantity


        }



    }





}