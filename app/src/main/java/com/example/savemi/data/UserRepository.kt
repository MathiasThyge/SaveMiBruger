package com.example.savemi.data


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File



data class User(
    val authentication: FirebaseUser,
    val name: String,
    val cpr: String,
    val image: Bitmap?,
    val medicines: List<String>,
    val donor: String,
    val allergies: List<String>,
    val emergencies: List<String>,
    val others: List<String>
)

interface UserInterface {
    fun login(username: String, password: String, onLogin: ((User?) -> Unit))
    fun logout()
}

class UserRepository(): UserInterface {
    private val logtag = UserRepository::class.simpleName
    val auth = FirebaseAuth.getInstance()


    /* private var userauth: FirebaseUser? = null
    var medicines =  ArrayList<Medicine>()
    lateinit var name: String
    lateinit var cpr: String
    lateinit var donor: String
    var allergies =  ArrayList<Allergi>()
    var emergency =  ArrayList<Emergency>()
    var others =  ArrayList<Other>()
    var image: Bitmap? = null*/

    override fun login(username: String, password: String, onLogin: ((User?) -> Unit)) {
        Log.d(logtag, "first login")
        Log.d(logtag, "Username: $username")
        Log.d(logtag, "Password: $password")

        val db = FirebaseDatabase.getInstance().reference.child("users")
        //var user: User
        //var userId: String

        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val userId = auth.currentUser?.uid.toString()
                    //userauth = auth.currentUser
                    Log.d(logtag, "userID: $userId")
                    db.child(userId).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            // Get Post object and use the values to update the UI
                            download(userId) {
                                val user = User(
                                    authentication = auth.currentUser!!,
                                    name = dataSnapshot.child("navn").getValue(true).toString(),
                                    cpr = dataSnapshot.child("persId").getValue(true).toString(),
                                    medicines = dataSnapshot.child("Medicin").value.toString()
                                        .getFirebaseList(),
                                    donor = dataSnapshot.child("doner").getValue(true).toString(),
                                    allergies = dataSnapshot.child("Allergier").value.toString()
                                        .getFirebaseList(),
                                    others = dataSnapshot.child("Andet").value.toString()
                                        .getFirebaseList(),
                                    image = it,
                                    emergencies = dataSnapshot.child("kontakinfo").toString()
                                        .getFirebaseList()
                                )

                                onLogin.invoke(user)
                            }


                            //Emergency
                            val emergencyName =
                                dataSnapshot.child("kontakinfo").child("navn").value.toString()
                            val emergencyPhone =
                                dataSnapshot.child("kontakinfo").child("telefon").value.toString()

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.d(logtag, "loadPost:onCancelled", databaseError.toException())
                            onLogin.invoke(null)
                        }
                    })

                }

                if (!task.isSuccessful) {
                    Log.d(logtag, "jeg fejlede med at logge ind")
                    onLogin.invoke(null)
                }
            }

        /* val usertoken = firbase.login(username, password)
        val firbaseUser = firmbase.gatData(usertoken)

        return User(
            firbaseUser.name,
            firbaseUser.downladProfileImage(),
            firbaseUser.dsm.map { Medicine(it.firebaseDsmName, it.firbaseDsmType) }
        )*/

    }

    fun upDataRepo(currentUser: FirebaseUser, onLogin: ((User?) -> Unit)) {
        val db = FirebaseDatabase.getInstance().reference.child("users")
        val userId = currentUser.uid
        db.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(logtag, dataSnapshot.child("Andet").value.toString())
                // Get Post object and use the values to update the UI
                download(userId) {
                    val user = User(
                        authentication = auth.currentUser!!,
                        name = dataSnapshot.child("navn").getValue(true).toString(),
                        cpr = dataSnapshot.child("persId").getValue(true).toString(),
                        medicines = dataSnapshot.child("Medicin").value.toString()
                            .getFirebaseList(),
                        donor = dataSnapshot.child("Donor").getValue(true).toString(),
                        allergies = dataSnapshot.child("Allergier").value.toString()
                            .getFirebaseList(),
                        others = dataSnapshot.child("Andet").value.toString().getFirebaseList(),
                        image = it,
                        emergencies = getEmengencyFirebaseList(
                            dataSnapshot.child("Kontaktperson").child("navn").value.toString(),
                            dataSnapshot.child("Kontaktperson").child("nummer").value.toString()
                        )
                    )

                    onLogin.invoke(user)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.d(logtag, "loadPost:onCancelled", databaseError.toException())
                onLogin.invoke(null)
            }
        })
    }


    override fun logout() = auth.signOut()

    companion object {

        fun getUserRepo(): UserRepository {
            return UserRepository()
        }
    }

    private fun download(userId: String, onImage: (Bitmap?) -> Unit) {
        val myStorage = FirebaseStorage.getInstance()
        val ref = myStorage.reference.child("$userId/image/ProfilePic.jpg")
        val file = File.createTempFile("ProfilePic", "jpg")

        Log.d(logtag, "file $file")
        ref.getFile(file).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                Log.d(logtag, "SUCCESS load and set profilepic repo")
                onImage.invoke(BitmapFactory.decodeFile(file.absolutePath))

            } else {
                Log.d(logtag, "Failed to load and set profilepic repo $task")
                onImage.invoke(null)
            }
        }
    }

    fun String.getFirebaseList(): List<String> {
        val emptyList = emptyList<String>()
        Log.d(logtag, "emptylist: $emptyList")
        val list = removePrefix("[").removeSuffix("]").split(",").toMutableList()
        var i = 0
        for (item in list){
            list[i] = item.trimIndent()
            i++
        }
        return if (list[0] == "null") emptyList
        else list


    }

    fun getEmengencyFirebaseList(navn: String, tlf: String): List<String> {

        val emptyList = emptyList<String>()
        val nameList = navn.removePrefix("[").removeSuffix("]").split(",")
        val tlfList = tlf.removePrefix("[").removeSuffix("]").split(",")
        Log.d(logtag, "$tlfList")
        return if (nameList[0] == "null") {
            emptyList
        } else {
            var i = 0
            var string = ""
            for (item in nameList) {
                if(nameList.size-1 >i ) {
                    string = item + ":" + " " + tlfList[i]+","

                }else{
                    string = item + ":" + " " + tlfList[i]

                }
                i++

            }
            val list = string.split(",")

            list
        }

    }
}

