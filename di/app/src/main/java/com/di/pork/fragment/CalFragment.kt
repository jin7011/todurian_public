package com.di.pork.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import com.di.pork.R
import com.di.pork.activity.MainActivity
import com.di.pork.data.Family
import com.di.pork.databinding.FragmentCalBinding
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Integer.min
import java.util.concurrent.TimeUnit
import kotlin.math.round

@AndroidEntryPoint
class CalFragment(
) : Fragment() {

    private val TAG = "CalFragment"
    private val disposable = CompositeDisposable()
    lateinit var binding: FragmentCalBinding
    var ratio1 = 0.0
    var ratio2 = 0.0
    var ratio3 = 0.0
    var ratio4 = 0.0
    var selectedInput = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalBinding.inflate(inflater,container,false)
        binding.spinner.run {
            adapter = ArrayAdapter.createFromResource(requireContext(),R.array.itemList,R.layout.item_spinner)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    selectedInput = position
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }

        editListener()

        return binding.root
    }

    private fun editListener(){

        disposable.add( getEditTextObservable(binding.calInputET1)
            .debounce(300,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setRatio(sum())
            })

        disposable.add( getEditTextObservable(binding.calInputET2)
            .debounce(300,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setRatio(sum())
            })

        disposable.add(getEditTextObservable(binding.calInputET3)
            .debounce(300,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setRatio(sum())
            })

        disposable.add(getEditTextObservable(binding.calInputET4)
            .debounce(300,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setRatio(sum())
            })

        disposable.add(getEditTextObservable(binding.calInputET5)
            .debounce(300,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(it.toString().trim().isNotEmpty() && it.toString() != "0"){
                    setWeight(it.toInt())
                }else{
                    (requireActivity() as MainActivity).toast("숫자를 입력해주세요.")
                }
            })

    }

    private fun sum():Double{
        return (getInput(binding.calInputET1) +
                getInput(binding.calInputET2) +
                getInput(binding.calInputET3) +
                getInput(binding.calInputET4)).toDouble()
    }

    private fun getInput(editText: EditText):Int{
        return if(editText.text.trim().isEmpty())
            0
        else
            editText.text.toString().trim().toInt()
    }

    @SuppressLint("SetTextI18n")
    fun setRatio(sum:Double){
        if(sum != 0.0) {
            val n = getN(sum)
            Log.e(TAG,"n: $n")

            ratio1 = round((getInput(binding.calInputET1) / sum) * n)
            ratio2 = round((getInput(binding.calInputET2) / sum) * n)
            ratio3 = round((getInput(binding.calInputET3) / sum) * n)
            ratio4 = round((getInput(binding.calInputET4) / sum) * n)

            binding.calRatioT.text = "반올림 비율:    $ratio1 : $ratio2 : $ratio3 : $ratio4"
        }
    }

    @SuppressLint("SetTextI18n")
    fun setWeight(input5:Int){
        var n = 0.0
        val input1 = getInput(binding.calInputET1).toDouble()
        val input2 = getInput(binding.calInputET2).toDouble()
        val input3 = getInput(binding.calInputET3).toDouble()
        val input4 = getInput(binding.calInputET4).toDouble()

        when(selectedInput){
            0 -> {
                if(input1 == 0.0){
                    (requireActivity() as MainActivity).toast("위에 입력한 재료를 선택해주세요.")
                    return
                }
                n = input5 / input1
            }
            1 -> {
                if(input2 == 0.0){
                    (requireActivity() as MainActivity).toast("위에 입력한 재료를 선택해주세요.")
                    return
                }
                n = input5 / input2}
            2 -> {
                if(input3 == 0.0){
                    (requireActivity() as MainActivity).toast("위에 입력한 재료를 선택해주세요.")
                    return
                }
                n = input5 / input3}
            3 -> {
                if(input4 == 0.0){
                    (requireActivity() as MainActivity).toast("위에 입력한 재료를 선택해주세요.")
                    return
                }
                n = input5 / input4}
        }
        binding.calResultT.text = "무게:    ${round(input1 * n)}g : ${round(input2 * n)}g : ${round(input3 * n)}g : ${round(input4 * n)}g"
    }

    fun getN(sum:Double):Int{
        val min = getMin()
        if(min == Int.MAX_VALUE)
            return 1

        Log.e(TAG,"min: $min")
        var aws = 1

        for(i in 1..sum.toInt()){
            val res = min/sum*i
            Log.e(TAG,"res: $res")

            if(res >= 1) {
                aws = i
                break
            }
        }
        return aws
    }

    fun getMin(): Int{
        var min = Int.MAX_VALUE
        val arr = ArrayList<Int>()
        arr.add(getInput(binding.calInputET1))
        arr.add(getInput(binding.calInputET2))
        arr.add(getInput(binding.calInputET3))
        arr.add(getInput(binding.calInputET4))

        for(i in arr){
            if(i>0)
                min = min(min,i)
        }

        return min
    }

    fun getEditTextObservable(editText: EditText): Observable<String> {
        return editText.textChanges()
            .map { obj: CharSequence -> obj.toString() }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}