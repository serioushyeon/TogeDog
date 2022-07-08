package com.example.dangdangee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel(){
    val count: MutableLiveData<Int> = MutableLiveData<Int>()
    init {
        count.value = 1
    }
    fun increaseCount() {
        count.value = (count.value ?: 0) + 1
    }

}