package com.example.savemi.home

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.savemi.data.UserRepository
import com.google.firebase.auth.FirebaseUser

enum class HomeDataType {
    NAME,
    CPR,
    BLOD,
    MEDICIN,
    ALLERGIE,
    DONER,
    EMERGENCY,
    OTHER
}

data class HomeDataElement (
    val text: String,
    val type: HomeDataType

)

data class HomeData(
    val profilePicture: Bitmap?,
    val elements: List<HomeDataElement>,
    val authentication: FirebaseUser?
)

class HomeViewModel: ViewModel() {
    private val logtag = HomeViewModel::class.simpleName
    private val repo = UserRepository.getUserRepo()
    private val modelViewHomeLiveData by lazy {
        MutableLiveData<HomeData?>()
    }

    fun getHomeData(): LiveData<HomeData?> = modelViewHomeLiveData



     fun login(username: String, password: String){
        Log.d(logtag,"first i login")
        val user = repo.login(username,password) { user ->
            if (user == null) {
                modelViewHomeLiveData.postValue(null)
            } else {
                Log.d(logtag, "user: $user")
                val list = mutableListOf<HomeDataElement>()
                list.add(HomeDataElement(user.name, HomeDataType.NAME))
                list.add(HomeDataElement(user.cpr, HomeDataType.CPR))
                list.add(HomeDataElement(user.donor, HomeDataType.BLOD))
                list.addAll(user.medicines.map { HomeDataElement(it, HomeDataType.MEDICIN) })
                list.addAll(user.allergies.map { HomeDataElement(it, HomeDataType.ALLERGIE) })
                list.addAll(user.emergencies.map { HomeDataElement(it, HomeDataType.EMERGENCY) })
                list.addAll(user.others.map { HomeDataElement(it, HomeDataType.OTHER) })
                modelViewHomeLiveData.postValue(HomeData(user.image, list, user.authentication))
            }
        }
    }

}