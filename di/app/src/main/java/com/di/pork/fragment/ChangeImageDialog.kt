package com.di.pork.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.di.pork.R
import com.di.pork.activity.MyInfoActivity
import com.di.pork.databinding.FragmentAddDialogBinding
import com.di.pork.databinding.FragmentChangeImageBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChangeImageDialog : DialogFragment() {

    @Inject
    lateinit var preference: PreferenceManager
    @Inject
    lateinit var repository : Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChangeImageBinding.inflate(inflater,container,false)

        binding.run {
            choiceImageT.setOnClickListener {
                (requireActivity() as MyInfoActivity).startDefaultGalleryApp()
                dismiss()
            }

            basicImageT.setOnClickListener {
                //todo 기본이미지로 변경
                CoroutineScope(Dispatchers.Main).launch {
                    repository.updateImage(preference.getString("id")!!,null)
                    (requireActivity() as MyInfoActivity).setView()
                    dismiss()
                }
            }
        }

        return binding.root
    }
}