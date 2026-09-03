package com.example.githubdemo.data.meals


object FavouriteManager {


    private val favouriteList =
        mutableSetOf<Int>()



    fun addFavourite(id:Int){

        favouriteList.add(id)

    }



    fun removeFavourite(id:Int){

        favouriteList.remove(id)

    }



    fun isFavourite(id:Int):Boolean{

        return favouriteList.contains(id)

    }



    fun toggleFavourite(id:Int){


        if(isFavourite(id)){


            removeFavourite(id)


        }else{


            addFavourite(id)

        }


    }


}