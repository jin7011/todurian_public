package com.di.pork.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.di.pork.R
import com.di.pork.databinding.FragmentAddDialogBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import com.di.pork.viewmodel.FamilyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddDialogFragment(val viewModel: FamilyViewModel) : DialogFragment() {

    @Inject
    lateinit var preference: PreferenceManager
    @Inject
    lateinit var repository : Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddDialogBinding.inflate(inflater,container,false)

        binding.inviteBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val familyId = binding.inviteNicknameET.text.toString()
                val myId = preference.getString("id")!!
                val mynickname = preference.getString("nickname")!!
                val myimage = preference.getString("image")!!
                val res = repository.getUser(familyId)

                for(user in viewModel.family.value!!.iterator()){
                    if(user.id == familyId){
                        Toast.makeText(requireContext(),"이미 등록된 아이디입니다.",Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                }

                if(repository.isExist(familyId)){
                    if(!res.invite){
                        Toast.makeText(requireContext(),"초대 거부상태인 아이디입니다.",Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    repository.addFamily(myId,mynickname,myimage,familyId)
                    Toast.makeText(requireContext(),"초대되었습니다.",Toast.LENGTH_SHORT).show()
                    viewModel.getFamily(myId)
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"존재하지 않는 아이디입니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }


        return binding.root
    }
}