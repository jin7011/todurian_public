package com.di.pork.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.di.pork.R
import com.di.pork.data.Family
import com.di.pork.databinding.ActivityMainBinding
import com.di.pork.databinding.ActivityMyInfoBinding
import com.di.pork.fragment.AddDialogFragment
import com.di.pork.fragment.ChangeImageDialog
import com.di.pork.fragment.ChangeNicknameFragment
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MyInfoActivity : AppCompatActivity() {

    private val activity = this
    lateinit var binding: ActivityMyInfoBinding
    @Inject lateinit var preferences: PreferenceManager
    @Inject lateinit var repository : Repository
    private val TAG = "MyInfoActivity"
    private val id by lazy { preferences.getString("id") }
    private val nickname by lazy { preferences.getString("nickname")}
    private val DEFAULT_GALLERY_REQUEST_CODE = 99

    override fun onRestart() {
        super.onRestart()
        Log.e(TAG,"onRestart")
    }

    override fun onResume() {
        super.onResume()
        setView()
        Log.e(TAG,"onResume")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"onCreate")
        setView()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent(this,MainActivity::class.java))
        super.onBackPressed()
    }

    fun setView(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_info)
        binding.run {
            idT.text = id
            nicknameT.text = nickname

            inviteSwitch.run {
                isChecked = preferences.getBoolean("invite")
                setOnCheckedChangeListener { _, isChecked ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val res = repository.updateInvite(preferences.getString("id")!!,isChecked)
                        if(res)
                            preferences.setBoolean("invite",isChecked)
                    }
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                val res = repository.getUser(id!!)
                if(res.result){
                    Glide.with(activity).load(activity.resources.getString(R.string.imageUrl) + res.image)
//                        .apply(RequestOptions().centerCrop().circleCrop())
                        .apply(RequestOptions().transforms(RoundedCorners(20),CenterCrop()))
                        .into(profileIV)
                    Log.e(TAG,"$res")
                }else{
                    Glide.with(activity).load(ContextCompat.getDrawable(activity,R.drawable.ic_person_24))
                        .into(profileIV)
                }
            }

            changeProfileT.setOnClickListener {
                val dialog = ChangeImageDialog()
                dialog.show(supportFragmentManager,"ChangeImageDialog")
            }

            changeNickT.setOnClickListener {
                val dialog = ChangeNicknameFragment(binding)
                dialog.show(supportFragmentManager,"ChangeNicknameFragment")
            }

            withdraw.setOnClickListener {
                withdrawDialog(id!!)
            }
        }
    }

    private fun withdrawDialog(id:String) {
        val dialog = android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog)

        dialog.setMessage("회원탈퇴를 하시겠습니까?\n\n\'사용자의 정보\'와 \'결제내역\'이 모두 삭제됩니다.")
            .setPositiveButton("탈퇴") { _, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    val res = repository.withdraw(id)
                    if(res){
                        toast("탈퇴되었습니다.")
                        preferences.clear()
                        loginActivity()
                    }else{
                        toast("실패하였습니다.")
                    }
                }
            }
            .setNeutralButton("아니오") { _, _ ->
            }
            .show()
    }

    fun startDefaultGalleryApp() {
        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        startActivityForResult(intent, DEFAULT_GALLERY_REQUEST_CODE)
    }

    // 갤러리 화면에서 이미지를 선택한 경우 현재 화면에 보여준다.
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            DEFAULT_GALLERY_REQUEST_CODE -> {

                CoroutineScope(Dispatchers.Main).launch {
                    val uri = data?.data as Uri
                    Log.e(TAG,"$uri")
                    val image = getBodyFromUri(uri)
                    Log.e(TAG,"onActivityResult $id")
                    repository.updateImage(id!!,image)
                    setView()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getBodyFromUri(uri: Uri): MultipartBody.Part {
        val path = getPath(uri)
        val file = File(path)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    @SuppressLint("Recycle", "Range")
    fun getPath(path: Uri): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(path, proj, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex("_data"))
        cursor.close()

        return path!!
    }

    fun loginActivity() {
        startActivity(Intent(this, LoginActivity::class.java).run {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
    }

    fun toast(str:String){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
    }
}