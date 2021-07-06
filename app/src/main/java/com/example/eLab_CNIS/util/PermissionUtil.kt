package com.example.eLab_CNIS.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

object PermissionUtil {

    private lateinit var content: Context
    private lateinit var activity: Activity
    private const val MY_PERMISSIONS_REQUEST = 3000

    //定义一个list，用于存储需要申请的权限
    private val permissionList = ArrayList<String>()

    fun init(content: Context, activity: Activity) {
        this.content = content
        this.activity = activity
        getPermission()
    }

    private fun getPermission() {
        permissionList.add(Manifest.permission.INTERNET)
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissionList.add(Manifest.permission.CAMERA)
        permissionList.add(Manifest.permission.READ_PHONE_STATE)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        checkAndRequestPermissions()
    }

    //调用封装好的申请权限的方法
    private fun checkAndRequestPermissions() {
        val list = ArrayList(permissionList)
        val it = list.iterator()
        while (it.hasNext()) {
            val permission = it.next()
            //检查权限是否已经申请
            val hasPermission = ContextCompat.checkSelfPermission(content, permission)
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                it.remove()
            }
        }
        /**
         *补充说明：ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
         * .RECORD_AUDIO);
         *对于原生Android，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale就会为true。
         *此时，用户可以弹出一个对话框，向用户解释为什么需要这项权限。
         *对于一些深度定制的系统，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale永远为false
         **/
        if (list.size == 0) {
            return
        }
        val permissions = list.toTypedArray()
        //正式请求权限
        ActivityCompat.requestPermissions(activity, permissions, this.MY_PERMISSIONS_REQUEST)
    }
}