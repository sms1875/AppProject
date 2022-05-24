package com.example.application.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel(){

    var id : String? =null
    var password :String?= null

   fun onLoginButtonClick(view: View){
       if (id.isNullOrEmpty()||password.isNullOrEmpty()){
           return
       }
   }

}