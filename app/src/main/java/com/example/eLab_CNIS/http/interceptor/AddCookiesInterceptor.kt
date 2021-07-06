package com.example.eLab_CNIS.http.interceptor


import com.example.eLab_CNIS.util.SpValueUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Describe : 从响应头里拿到cookie并存起来，后面的每次请求再添加到请求头里
 */
class AddCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val stringSet = SpValueUtil.getStringSet("Cookie")
        val token = SpValueUtil.getString("token","")
        if(token.isNotEmpty())
        {
            builder.addHeader("authorization", "Bearer $token")
        }
        for (cookie in stringSet) {
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }
}