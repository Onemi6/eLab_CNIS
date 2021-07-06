package com.example.eLab_CNIS.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import java.net.NetworkInterface
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object Util {
    private lateinit var mApp: Application
    // 两次点击按钮之间的点击间隔不能少于2000毫秒
    private const val MIN_CLICK_DELAY_TIME = 2000
    private var lastClickTime: Long = 0

    @Deprecated("简化调用，使用init(app)即可", ReplaceWith("Util.init(app)"))
    fun initialize(app: Application) {
        mApp = app
        app.registerActivityLifecycleCallbacks(ActivityUtil.activityLifecycleCallbacks)
    }

    fun init(app: Application) {
        mApp = app
        app.registerActivityLifecycleCallbacks(ActivityUtil.activityLifecycleCallbacks)
    }

    @Deprecated("简化调用，使用getApp()即可", ReplaceWith("Util.getApp()"))
    fun getApplication(): Application {
        return mApp
    }

    fun getApp(): Application {
        if (this::mApp.isInitialized) {
            return mApp
        } else {
            throw UninitializedPropertyAccessException("Util未在Application中初始化")
        }
    }

    /**
     * 获取屏幕宽度
     */
    @Deprecated(
        "拆分处理，使用DisplayUtil.getScreenWidth()即可",
        ReplaceWith("DisplayUtil.getScreenWidth()")
    )
    fun getScreenWidth(): Int {
        val dm = DisplayMetrics()
        ActivityUtil.currentActivity!!.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    @Deprecated(
        "拆分处理，使用DisplayUtil.getScreenHeight()即可",
        ReplaceWith("DisplayUtil.getScreenHeight()")
    )
    fun getScreenHeight(): Int {
        val dm = DisplayMetrics()
        ActivityUtil.currentActivity!!.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 根据时间休眠然后关闭当前页面
     * 比如：5秒自动返回
     * 或者只需要后台给一个结果而已
     */
    fun finishBySleep(millis: Long) {
        object : Thread() {
            override fun run() {
                try {
                    sleep(millis)
                    ActivityUtil.currentActivity?.finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    /**
     * 获取版本名
     */
    fun getVersionName(): String? {
        return try {
            val packageManager = getApp().packageManager
            val packageInfo = packageManager.getPackageInfo(getApp().packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取版本号
     */
    fun getVersionCode(): Int {
        return try {
            val packageManager = getApp().packageManager
            val packageInfo = packageManager.getPackageInfo(getApp().packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 检验手机号
     */
    fun checkPhoneNumber(number: String?): Boolean {
        var p: Pattern? = null
        var m: Matcher? = null
        var b = false
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$")
        m = p.matcher(number)
        b = m.matches()
        return b
    }

    /**
     * MD5加密
     */
    fun MD5(data: String): String {
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        md5!!.update(data.toByteArray())
        val m = md5.digest()
        return Base64.encodeToString(m, Base64.DEFAULT)
    }

    /**
     * dp2px
     */
    @Deprecated("拆分处理，使用DisplayUtil.dp2px()即可", ReplaceWith("DisplayUtil.dp2px(dp)"))
    fun dp2px(dp: Float): Int {
        val density = getApp().resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    /**
     * px2dp
     */
    @Deprecated("拆分处理，使用DisplayUtil.px2dp()即可", ReplaceWith("DisplayUtil.px2dp(px)"))
    fun px2dp(px: Int): Float {
        val density = getApp().resources.displayMetrics.density
        return px / density
    }

    /**
     * 复制文本到粘贴板
     */
    fun copyToClipboard(text: String?) {
        val cm = getApp().getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText(getApp().packageName, text))
    }

    /**
     * 字体高亮
     */
    fun foreground(view: View?, color: Int, start: Int, end: Int): View? {
        if (view is Button) {
            val btn = view
            // 获取文字
            val span: Spannable = SpannableString(btn.text.toString())
            //设置颜色和起始位置
            span.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            btn.text = span
            return btn
        } else if (view is TextView) { //EditText extends TextView
            val span: Spannable = SpannableString(view.text.toString())
            span.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            view.text = span
            return view
        }
        return null
    }

    /**
     * 弹出软键盘
     */
    fun showSoftKeyboard(view: View) {
        val inputManger =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManger.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 关闭软键盘
     */
    fun closeSoftKeyboard() {
        val inputManger =
            ActivityUtil.currentActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManger.hideSoftInputFromWindow(
            ActivityUtil.currentActivity!!.window.decorView.windowToken,
            0
        )
    }

    /**
     * 是否有sim卡 即设备是否可以拨打电话等
     */
    fun hasSim(): Boolean {
        val telephonyManager =
            getApp().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var result = true
        when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_ABSENT -> result = false
            TelephonyManager.SIM_STATE_UNKNOWN -> result = false
        }
        return result
    }



    fun isFastClick(): Boolean {
        var flag = false
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            flag = true
        }
        lastClickTime = curClickTime
        return flag
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val connectivity =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //新版本调用方法获取网络状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networks = connectivity.allNetworks
            var networkInfo: NetworkInfo
            for (mNetwork in networks) {
                networkInfo = connectivity.getNetworkInfo(mNetwork)!!
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        } else {
            //否则调用旧版本方法
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null) {
                    for (anInfo in info) {
                        if (anInfo.isConnected) {
                            // 当前网络是连接的
                            if (anInfo.state == NetworkInfo.State.CONNECTED) {
                                // 当前所连接的网络可用
                                Log.d("Network", "NETWORKNAME: " + anInfo.typeName)
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }


    //获取手机的IMEI
    fun getIMEI(context: Context): String? {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            val method = manager.javaClass.getMethod("getImei", Int::class.javaPrimitiveType)
            val imei1 = method.invoke(manager, 0) as String
            val imei2 = method.invoke(manager, 1) as String
            if (TextUtils.isEmpty(imei2)) {
                return imei1
            }
            if (!TextUtils.isEmpty(imei1)) {
                //因为手机卡插在不同位置，获取到的imei1和imei2值会交换，所以取它们的最小值,保证拿到的imei都是同一个
                var imei = ""
                imei = if (imei1.compareTo(imei2) >= 0) {
                    imei1
                } else {
                    imei2
                }
                return imei
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    fun getMac(): String? {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = nif.hardwareAddress ?: return null
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    /**
     * 获取SN
     * @return
     */
    @SuppressLint("PrivateApi", "HardwareIds")
    fun getSN(): String {
        var serial = ""
        //通过反射获取sn号
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
            if (serial != "" && serial != "unknown") return serial

            //9.0及以上无法获取到sn，此方法为补充，能够获取到多数高版本手机 sn
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) serial = Build.getSerial()
        } catch (e: Exception) {
            serial = ""
        }
        return serial
    }
}