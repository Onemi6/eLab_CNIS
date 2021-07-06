package com.example.eLab_CNIS.util

object ClickUtil {
    // 两次点击按钮之间的点击间隔不能少于2000毫秒
    private val MIN_CLICK_DELAY_TIME = 2000
    private var lastClickTime: Long = 0

    fun isFastClick(): Boolean {
        var flag = false
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            flag = true
        }
        lastClickTime = curClickTime
        return flag
    }
}