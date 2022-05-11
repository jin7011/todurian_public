package com.di.pork.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.di.pork.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {

    val TAG = PermissionActivity::class.java.simpleName
    private val permissionRequestCode = 100
    private var permissions  = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        if (!isGrantedPermission()) {
            createPermissionDialog()
        } else {
            Activity(LoginActivity::class.java)
        }
    }

    fun Activity(c: Class<*>?) {
        val i = Intent(this, c)
        i.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        this.startActivity(i)
        finish()
    }

    fun isGrantedPermission(): Boolean {
        permissions.clear()

        //  카메라
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.CAMERA)
        }

        // READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // 권한이 없으면 permissions ArrayList에 추가 해 놓는다
        // 즉 리턴값은 허가 받아야 할 권한을 ArrayList 저장해 놓고 권한을 요청여부를 리턴 한다
        return permissions.size == 0
    }

    fun createPermissionDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("권한 요청 안내")
        builder.setMessage("다음과 같은 권한이 필요 합니다 \n[저장소],[카메라]")
        builder.setCancelable(false)
        builder.setPositiveButton("확인",
            DialogInterface.OnClickListener { dialog, which -> requestPermission() })
        builder.show()
    }

    private fun requestPermission() {
        if (permissions.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(), permissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var grantedCnt = 0

        when (requestCode) {
            permissionRequestCode ->
                if (grantResults.isNotEmpty()) {
                    for (g in grantResults) {
                        if (g == PackageManager.PERMISSION_GRANTED) {
                            // 허용한 권한 갯수를 카운트 한다
                            grantedCnt++
                        }
                    }
                }
        }

        if (grantedCnt == permissions.size) {
            Log.e(TAG,"grantedsize: $grantedCnt permissionssize: ${permissions.size}")
            Activity(LoginActivity::class.java)
        } else {
            // 필요한 권한을 전부 허용 하지 않았을 경우 다른 방법으로 재요청 처리를 해 보았다
            Log.e(TAG,"grantedsize: $grantedCnt permissionssize: ${permissions.size}")
            createSettingsDialog()
        }
    }

    private fun createSettingsDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("앱을 원할이 이용하기 위해서 저장소, 카메라 권한이 필요 합니다.")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "확인"
        ) { dialogInterface, i ->
            requestPermission()
            val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:$packageName"))
            startActivity(intent)
        }
        builder.setNegativeButton(
            "닫기"
        ) { dialogInterface, i ->
            dialogInterface.dismiss()
            finish()
        }
        builder.show()
    }

}