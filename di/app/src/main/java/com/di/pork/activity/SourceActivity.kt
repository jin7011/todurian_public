package com.di.pork.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.di.pork.R
import com.di.pork.adapter.SourceAdapter
import com.di.pork.data.Source
import com.di.pork.databinding.ActivitySourceBinding

class SourceActivity : AppCompatActivity() {

    lateinit var binding: ActivitySourceBinding
    private val TAG = "SourceActivity"
    private val sourceList = ArrayList<Source>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(this)
    }

    fun setView(context: Context){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_source)
        add()
        val adapter = SourceAdapter(context,sourceList)
        binding.sourceListView.adapter = adapter
    }

    fun add(){
        addList("OkHttp,Retrofit2","https://github.com/square/okhttp","2014","square")
        addList("Apach Commons Lang","https://commons.apache.org/lang/","2002-2011","The Apach Software")
        addList("gson","https://github.com/google/gson","2008","Google")
        addList("LicensesDialog","https://psdev.de/LicensesDialog","2013","Philip Sciffer")
        addList("MpChart","https://github.com/PhilJay/MPAndroidChart","2020","Philipp Jahoda")
        addList("Rxjava","https://github.com/ReactiveX/RxJava","2016-present"," RxJava Contributors.")
    }

    fun addList(name:String,link:String,year:String,company:String){
        val content =
            "Copyright $year $company, Inc.\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
        sourceList.add(Source(name,link,content))
    }
}