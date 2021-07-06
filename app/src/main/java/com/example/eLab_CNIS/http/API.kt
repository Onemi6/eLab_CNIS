package com.example.eLab_CNIS.http

import com.example.eLab_CNIS.models.Login
import com.example.eLab_CNIS.models.Tasks
import io.reactivex.Observable
import retrofit2.http.*

class API {
    companion object {
        const val BASE_URL = "http://cnis.cloudlimslab.com/"
    }

    interface WebApi {
        //登录
        @POST("Account/doLogin")
        fun login(@Body any: Any): Observable<List<Login>>

        //任务列表
        @FormUrlEncoded
        @POST("Search/doActionFromRequestStr")
        fun getTasks(@Field("Search_SQL") Search_SQL: String): Observable<Tasks>
    }
}