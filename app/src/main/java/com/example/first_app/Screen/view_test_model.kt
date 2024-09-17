package com.example.first_app.Screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class view_test_model:ViewModel() {
    private val name_= MutableLiveData<String>()
    val name:LiveData<String> = name_

    fun setName(name:String){
        name_.value = name
    }
    private val sur_name_ = MutableLiveData<String>()
    val sur_name:LiveData<String> = sur_name_

    fun change_Surname(newsur_name:String){
        sur_name_.value = newsur_name
    }

}