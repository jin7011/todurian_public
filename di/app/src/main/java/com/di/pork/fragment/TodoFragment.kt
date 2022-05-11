package com.di.pork.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.di.pork.R
import com.di.pork.adapter.TodoAdapter
import com.di.pork.data.Family
import com.di.pork.data.User
import com.di.pork.databinding.FragmentTodoBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import com.di.pork.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TodoFragment(
//    private val user:Family
) : Fragment() {

    companion object{
        fun newInstance(user: Family): TodoFragment {
            val fragment = TodoFragment()
            val args = Bundle()
            args.putParcelable("user",user)
            fragment.arguments = args

            return fragment
        }
    }

    private val user by lazy {
        requireArguments().getParcelable<Family>("user")!!
    }

    @Inject lateinit var preference: PreferenceManager
    @Inject lateinit var repository : Repository
//    private val args: TodoFragmentArgs by navArgs()
    private val viewModel: TodoViewModel by viewModels()
    private val TAG = "TodoFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"${user.id}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTodoBinding.inflate(inflater,container,false)
        val adapter = TodoAdapter(ArrayList(),object : TodoAdapter.LongClick{
            override fun longClick(id: String, content: String) {
                    viewModel.run{
                        delTodo(id,content)
                        Toast.makeText(requireContext(),"삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
            }
        },object : TodoAdapter.OnClick{
            override fun onClick(id: String, content: String) {
                    viewModel.doTodo(id,content)
                }
        })
        binding.todoListView.adapter = adapter

        binding.todoBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val nickname = preference.getString("nickname")!!
                val content = binding.todoET.text.toString()

                viewModel.addTodo(user.id,nickname,content)
                Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
                binding.todoET.setText("")
            }
        }

        viewModel.run {
            todoList.observe(viewLifecycleOwner) {
                binding.hasTodo = it.isNotEmpty()
                adapter.run {
                    todoList = it
                    notifyDataSetChanged()
                }
            }
            getTodoList(user.id) //처음 데이터 적용
        }

        return binding.root
    }
}