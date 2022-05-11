package com.di.pork.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.di.pork.R
import com.di.pork.data.User
import com.di.pork.databinding.ActivitySigninAcivityBinding
import com.di.pork.model.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.regex.Pattern
import javax.inject.Inject


@AndroidEntryPoint
class SigninActivity: AppCompatActivity(){

    @Inject lateinit var repository : Repository
    private lateinit var binding:ActivitySigninAcivityBinding
    private var image:MultipartBody.Part? = null
    private val DEFAULT_GALLERY_REQUEST_CODE = 99
    private val TAG = "SigninActivity"
    private fun Int.getResourceUri(context: Context): Uri {
        return context.resources.let {
            Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(it.getResourcePackageName(this))		// it : resources, this : ResId(Int)
                .appendPath(it.getResourceTypeName(this))		// it : resources, this : ResId(Int)
                .appendPath(it.getResourceEntryName(this))		// it : resources, this : ResId(Int)
                .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
    }

    private fun setView(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signin_acivity)

        binding.siginBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch{
                if(check()){
                    val user = User(binding.idET.text.toString(),binding.passET.text.toString(),image,binding.nicknameET.text.toString())
                    repository.signin(user)
                    toast("가입되었습니다.")
                    startActivity(Intent(applicationContext,LoginActivity::class.java))
                    finish()
                }
            }
        }

        binding.profileIV.setOnClickListener {
            startDefaultGalleryApp()
        }
    }

    private fun startDefaultGalleryApp() {
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
//                val source = ImageDecoder.createSource(this.contentResolver,uri)
//                // 이미지 URI를 가지고 하고 싶은거 하면 된다.
//                val bitmap = ImageDecoder.decodeBitmap(source)

                CoroutineScope(Dispatchers.Main).launch {
                    val uri = data?.data as Uri
                    Log.e(TAG,"$uri")
                    image = getBodyFromUri(uri)
                    binding.profileIV.setImageURI(uri)
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

    private suspend fun check():Boolean{

        binding.run {
            if(!Pattern.matches("^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{2,}\$", nicknameET.text.toString())){
                Log.e(TAG,nicknameET.text.toString())
                toast("닉네임을 다시 설정해주세요.")
                return false
            }

            if(!Pattern.matches("^[a-zA-Z0-9\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$", idET.text.toString())){
                toast("아이디를 다시 설정해주세요.")
                return false
            }else if(repository.isExist(idET.text.toString())){
                toast("이미 존재하는 아이디입니다.")
                return false
            }

            if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9]).{4,12}.\$", passET.text.toString())){
                toast("비밀번호를 다시 설정해주세요.")
                return false
            }

            if(passCheckET.text.toString() != passET.text.toString()){
                toast("비밀번호가 다릅니다.")
                return false
            }
        }

        return true
    }

    fun resize(bitmap:Bitmap):Bitmap {
        val config = resources.configuration.smallestScreenWidthDp

        return when {
            config >= 800 -> Bitmap.createScaledBitmap(bitmap,400,240,true)
            config >= 600 -> Bitmap.createScaledBitmap(bitmap,300,180,true)
            config >= 400 -> Bitmap.createScaledBitmap(bitmap,200,120,true)
            config >= 360 -> Bitmap.createScaledBitmap(bitmap,180,108,true)
            else -> Bitmap.createScaledBitmap(bitmap,160,96,true)
        }
    }

    fun toast(str:String){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
    }
}
