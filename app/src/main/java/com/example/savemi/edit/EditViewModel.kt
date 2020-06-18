package com.example.savemi.edit



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.savemi.data.UserRepository
import com.google.firebase.auth.FirebaseUser


enum class EditDataType {
    NAME,
    CPR,
    BLOD,
    MEDICIN,
    ALLERGIE,
    DONER,
    EMERGENCY,
    OTHER
}

data class EditDataElement (
    val text: String,
    val type: EditDataType,
    val count: Int
)

data class EditData(
    val elements: List<EditDataElement>,
    val authentication: FirebaseUser?

)

class EditViewModel: ViewModel() {
    private val logtag = EditViewModel::class.simpleName
    private val repo = UserRepository.getUserRepo()
    private val modelViewEditLiveData by lazy {
        MutableLiveData<EditData?>()
    }

    fun getEditData(): LiveData<EditData?> = modelViewEditLiveData


       fun editUpdateRepo( currentUser: FirebaseUser){
           var medicin = 0
           var allergi = 0
           var emergency = 0
           var other = 0
           repo.upDataRepo(currentUser) { user ->
            if (user == null) {
                modelViewEditLiveData.postValue(null)
            } else {
                Log.d(logtag, "user: $user")
                val list = mutableListOf<EditDataElement>()
                list.add(EditDataElement(user.name, EditDataType.NAME,0))
                list.add(EditDataElement(user.cpr, EditDataType.CPR,0))
                list.add(EditDataElement(user.blood, EditDataType.BLOD,0))
                list.add(EditDataElement(user.donor,EditDataType.DONER,0))
                list.addAll(user.medicines.map { EditDataElement(it, EditDataType.MEDICIN,medicin++) })
                list.addAll(user.allergies.map { EditDataElement(it, EditDataType.ALLERGIE,allergi++) })
                list.addAll(user.emergencies.map { EditDataElement(it, EditDataType.EMERGENCY,emergency++) })
                list.addAll(user.others.map { EditDataElement(it, EditDataType.OTHER,other++) })
                modelViewEditLiveData.postValue(EditData(list, user.authentication))
                medicin = 0
                allergi = 0
                emergency = 0
                other = 0
            }
        }
    }

    fun onChangesValues(uid: String, newValue: String, type: String, count: Int): String{
        var boolean = ""
        Log.d(logtag, "Viewmodel")
        repo.changeUserValues(uid,newValue,type,count) {
            boolean = it.toString()
            Log.d(logtag, "it:${it}")
        }

        return boolean
    }

}