package com.di.pork.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.di.pork.R
import com.di.pork.data.Family
import com.di.pork.databinding.FragmentDelDialogBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import com.di.pork.viewmodel.FamilyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DelDialogFragment(val viewModel: FamilyViewModel,val family:Family) : DialogFragment() {

    @Inject
    lateinit var preference: PreferenceManager
    @Inject
    lateinit var repository : Repository

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDelDialogBinding.inflate(inflater,container,false)
        binding.changeNickET.text = "${family.nickname} 님을 목록에서 삭제하시겠습니까?"

        binding.delBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val myId = preference.getString("id")!!
                val res = repository.delFamily(myId,family.id)
                if(res)
                    Toast.makeText(requireContext(),"삭제되었습니다.", Toast.LENGTH_SHORT).show()
                viewModel.getFamily(myId)
                dismiss()
            }
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}