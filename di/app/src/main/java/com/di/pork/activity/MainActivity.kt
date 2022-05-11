package com.di.pork.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.di.pork.R
import com.di.pork.databinding.ActivityMainBinding
import com.di.pork.fragment.HomeViewPagerFragment
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding:ActivityMainBinding
    private var backKeyPressedTime: Long = 0
    @Inject lateinit var preferences: PreferenceManager
    @Inject lateinit var repository : Repository
    private val TAG = "MainActivity"
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setView()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG,"onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG,"onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e(TAG,"onRestart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"onCreate")
        setView()
    }

    fun setView(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host, HomeViewPagerFragment())
            .commit()

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                val id = preferences.getString("id")
                val nickname = preferences.getString("nickname")

                if (!id.isNullOrEmpty()) { //로그인 상태라면 navi-drawer의 header에 이름과 email을 적자
                    binding.navigationView.run {
                        menu.clear()
                        inflateMenu(R.menu.navi_usermenu)
                    }
                    val headerview = binding.navigationView.getHeaderView(0)
                    (headerview.findViewById<TextView>(R.id.id_T)).text = id
                    (headerview.findViewById<TextView>(R.id.nickname_T)).text = nickname
                } else {
                }
            }

            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })

        binding.navigationView.setNavigationItemSelectedListener(this)

        binding.naviFbtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {
                toast("로그아웃 되었습니다.")
                preferences.clear()
                loginActivity()
            }

            R.id.item_info -> {
                openMyInfoActivityForResult()
            }

            R.id.item_private -> {
                startActivity(Intent(this,PolicyActivity::class.java).apply {
                    putExtra("policy","private")
                })
            }

            R.id.item_policy -> {
                startActivity(Intent(this,PolicyActivity::class.java).apply {
                    putExtra("policy","policy")
                })
            }

            R.id.item_source -> {
                startActivity(Intent(this,SourceActivity::class.java))
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    private fun loginActivity() {
        startActivity(Intent(this, LoginActivity::class.java).run {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
        finish()
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            return
        }

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            toast("\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.")
            return
        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            //아래 3줄은 프로세스 종료
//            moveTaskToBack(true)
//            android.os.Process.killProcess(android.os.Process.myPid())
//            exitProcess(1)
            finish()
        }
    }

    fun openMyInfoActivityForResult() {
        val intent = Intent(this, MyInfoActivity::class.java)
        resultLauncher.launch(intent)
    }

    fun toast(str: String) {
        android.widget.Toast.makeText(this, str, android.widget.Toast.LENGTH_SHORT).show();
    }
}