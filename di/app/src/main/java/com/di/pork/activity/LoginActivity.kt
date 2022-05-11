package com.di.pork.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.di.pork.R
import com.di.pork.databinding.ActivityLoginBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject lateinit var preference: PreferenceManager
    @Inject lateinit var repository: Repository
    lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"

    override fun onStart() {
        super.onStart()

        if(!preference.getString("id").isNullOrEmpty()){
            toast("자동로그인 되었습니다.")
            activityStart(MainActivity::class.java)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
    }

    private fun setView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.run {
            btnLogin.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val response = repository.login(idET.text.toString(), pwET.text.toString())
                    if(response.result){
                        toast("로그인 되었습니다.")
                        preference.run {
                            Log.e(TAG,"$response")
                            setString("id",response.id)
                            setString("nickname",response.nickname)
                            setString("image",response.image)
                            setBoolean("invite",response.invite)
                        }
                        activityStart(MainActivity::class.java)
                        finish()
                    }else{
                        toast("아이디 또는 비밀번호가 틀렸습니다.")
                    }
                }
            }

            btnRegister.setOnClickListener {
                activityStart(SigninActivity::class.java)
            }
        }
    }

    private fun activityStart(c:Class<*>) {
        val i = Intent(this, c)
        this.startActivity(i)
    }

    fun toast(str:String){
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show()
    }
}