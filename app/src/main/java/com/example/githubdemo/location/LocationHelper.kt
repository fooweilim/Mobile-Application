package com.example.githubdemo.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


object LocationHelper {


    suspend fun getCurrentLocation(
        context: Context
    ): String {


        if(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ){

            return "Location unavailable"

        }



        val client =
            LocationServices
                .getFusedLocationProviderClient(
                    context
                )



        return suspendCancellableCoroutine { cont ->


            client.lastLocation
                .addOnSuccessListener { location ->


                    if(location != null){


                        cont.resume(

                            "${location.latitude}, ${location.longitude}"

                        )


                    }else{


                        cont.resume(
                            "Location unavailable"
                        )


                    }


                }


        }


    }


}