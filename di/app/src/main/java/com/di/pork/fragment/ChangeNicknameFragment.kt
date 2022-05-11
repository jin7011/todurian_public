package com.di.pork.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.di.pork.R
import com.di.pork.databinding.ActivityMyInfoBinding
import com.di.pork.databinding.FragmentAddDialogBinding
import com.di.pork.databinding.FragmentChangeNicknameBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import com.di.pork.viewmodel.FamilyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChangeNicknameFragment(
    private val myInfoBinding: ActivityMyInfoBinding
    ) : DialogFragment() {

    @Inject
    lateinit var preference: PreferenceManager
    @Inject
    lateinit var repository : Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChangeNicknameBinding.inflate(inflater,container,false)

        binding.run{
            changeBtn.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val newNick = changeNickET.text.toString()
                    val id = preference.getString("id")
                    val nickname = preference.getString("nickname")

                    if(newNick == nickname){
                        Toast.makeText(requireContext(),"같은 닉네임입니다.",Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    val res = repository.updateNick(id!!,newNick)

                    if(res){
                        Toast.makeText(requireContext(),"변경되었습니다.",Toast.LENGTH_SHORT).show()
                        myInfoBinding.nicknameT.text = newNick
                        preference.setString("nickname",newNick)
                        dismiss()
                    }
                }
            }

            cancelBtn.setOnClickListener {
                dismiss()
            }
        }
        return binding.root
    }
}