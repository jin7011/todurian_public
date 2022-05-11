package com.di.pork.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.di.pork.R
import com.di.pork.databinding.ActivityPolicyBinding

class PolicyActivity : AppCompatActivity() {

    private val policy by lazy { //액티비티 하나로 이용약관,개인정보처리,오픈소스 돌려 쓸 예정
        intent.extras?.getString("policy")!!
    }
    private lateinit var binding: ActivityPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
    }

    fun setView(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_policy)

        when(policy){

            "policy" -> {
                binding.policyT.text = getText(R.string.policy)
            }

            "private" -> {
                binding.policyT.text = getText(R.string.user_private)
            }
        }
    }
}