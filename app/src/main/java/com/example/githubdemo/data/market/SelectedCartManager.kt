package com.example.githubdemo.data.market


import com.example.githubdemo.model.market.CartProduct



object SelectedCartManager {



    private var selectedItems:
            List<CartProduct> = emptyList()





    fun saveSelectedCart(

        items:List<CartProduct>

    ){

        selectedItems = items

    }







    fun getSelectedCart():

            List<CartProduct>{


        return selectedItems


    }







    fun clear(){


        selectedItems =
            emptyList()


    }




}