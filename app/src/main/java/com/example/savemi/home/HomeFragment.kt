package com.example.savemi.home

import android.Manifest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savemi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home_new.*
import kotlinx.android.synthetic.main.fragment_login.*

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private val logtag = HomeFragment::class.simpleName
    private lateinit var database: DatabaseReference
    private lateinit var mystorage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    var bitmap: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val view = inflater.inflate(R.layout.fragment_home_new,container,false)
        /*if(currentUser != null){
            //downloadAndsetProfilePic()
            if (bitmap == null)
                download()
        }*/
        if(currentUser == null) {
            Log.d(logtag, "USER DOES NOT EXIST: $currentUser")
            findNavController().navigate(R.id.action_homeFragment3_to_loginFragment)
        }

        if (bitmap != null)
            view.findViewById<ImageView>(R.id.home_ProfilePic).setImageBitmap(bitmap)



        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        view.findViewById<Button>(R.id.logout_button).setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment3_to_loginFragment)
        }
        view.findViewById<ImageView>(R.id.home_settings).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment3_to_settingsFragment)
        }

        val homeListAdaptor = HomeAdaptor()
        Log.d(logtag, "$homeListAdaptor")
        val list: RecyclerView = view.findViewById(R.id.home_RecyclerView)
        list.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        list.adapter = homeListAdaptor
        //hardcoded
       /* homeListAdaptor.list.addAll(listOf(
            HomeDataElement("mathias", HomeDataType.NAME),
            HomeDataElement("180497", HomeDataType.CPR),
            HomeDataElement("o+",HomeDataType.BLOD)
        ))
        homeListAdaptor.notifyDataSetChanged()*/

        val model: HomeViewModel by activityViewModels()

        model.getHomeData().observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            homeListAdaptor.list.clear()
            Log.d(logtag,"elements: ${it.elements}")
            homeListAdaptor.list.addAll(it.elements)
            homeListAdaptor.notifyDataSetChanged()
            Log.d(logtag, "in observe")

            view.findViewById<ImageView>(R.id.home_ProfilePic).setImageBitmap(it.profilePicture)
        })




        home_ChangeProfilePic.setOnClickListener{
            // Check runtime permission
            Log.d(logtag, "Version SDK int" + VERSION.SDK_INT)
            Log.d(logtag, "Version Codes M" + VERSION_CODES.M)

            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (activity?.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions,
                        PERMISSION_CODE
                    )
                }
                else{  chooseImageFromGallery()
                   }//permission already granted

            } else{ chooseImageFromGallery()
                } //system OS is < Marshmallow
        }


    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    private fun chooseImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }
    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    chooseImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                        home_ProfilePic.setImageURI(data?.data)
                        uploadProfilePic(data?.data)
        }
    }

    private fun uploadProfilePic(data: Uri?) {

        auth = FirebaseAuth.getInstance()
        mystorage = FirebaseStorage.getInstance()
        val storageRef = mystorage.reference
        val user = auth.currentUser?.uid.toString()
        if (auth.currentUser != null) {
            val UserRef = storageRef.child(user)
            val ImageRef = UserRef.child("image")
            val profilePicRef = ImageRef.child("ProfilePic.jpg")
            if (data != null) {
                val uploadtask = profilePicRef.putFile(data)
                uploadtask.addOnFailureListener {
                    Log.d(logtag, "FEJL med at uploade billede til storage")
                }
                uploadtask.addOnSuccessListener {
                    Log.d(logtag, "SUCCESS med at uploade billede til storage")
                }
            }
        }else{
            Log.d(logtag, "Uploade Profile pic - Ingen auth ")
        }
    }
    /*private fun download(){
        auth = FirebaseAuth.getInstance()
        mystorage = FirebaseStorage.getInstance()
        val storageRef = mystorage.reference
        val user = auth.currentUser?.uid.toString()

        val ref = mystorage.reference.child("$user/image/ProfilePic.jpg")

        val file = File.createTempFile("ProfilePic","jpg")

        Log.d(logtag,"file $file")
        ref.getFile(file).addOnCompleteListener(){task ->
            if(task.isSuccessful){
                bitmap = BitmapFactory.decodeFile(file.absolutePath)
                Log.d(logtag,"file path"+ file.absolutePath)
                home_cardView.home_ProfilePic.setImageBitmap(bitmap)
                Log.d(logtag, "SUCCESS load and set profilepic")

            }else Log.d(logtag, "Failed to load and set profilepic $task")
        }

    }
*/

    }










