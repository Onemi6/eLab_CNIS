package com.example.eLab_CNIS.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dou361.dialogui.DialogUIUtils
import com.example.eLab_CNIS.R
import com.example.eLab_CNIS.adapter.TaskAdapter
import com.example.eLab_CNIS.http.RetrofitService
import com.example.eLab_CNIS.models.Tasks
import com.example.eLab_CNIS.util.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MainActivity : AppCompatActivity() {

    private var mExitTime: Long = 0
    private var _context: Context? = null
    private var taskAdapter: TaskAdapter? = null
    private var tasks: Tasks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _context = this

        initView()
        /*设置监听器*/
        setListener()

        attemptGetTasks()
    }

    private fun initView() {
        Util.init(this.application)
        /*设置ActionBar
        *不使用toolbar自带的标题
         */
        toolbar_main.title = ""
        setSupportActionBar(toolbar_main)
        /*显示Home图标*/
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        /*设置ToolBar标题，使用TestView显示*/
        tv_title_main.text = resources.getString(R.string.TasksList)

        val headerView: View = nav_view.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tv_user_name)
        val tvAppVersionName = headerView.findViewById<TextView>(R.id.tv_app_versionName)
        val name: String = SpValueUtil.getString("NAME")
        val versionName: String? = Util.getVersionName()
        tvUserName.text = String.format(resources.getString(R.string.user_name), name)
        tvAppVersionName.text = String.format(
            resources.getString(R.string.app_versionName), versionName
        )
        /*设置Drawerlayout的开关,并且和Home图标联动*/
        val mToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar_main, 0, 0)
        drawer_layout.addDrawerListener(mToggle)
        /*同步drawerlayout的状态*/
        mToggle.syncState()

        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
        //rv_tasks.setHasFixedSize(true);

        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
        val layoutManager = LinearLayoutManager(this)
        //设置RecyclerView 布局
        //设置RecyclerView 布局
        rv_mainInfo_add.layoutManager = layoutManager
        //设置Adapter
        //设置Adapter
        taskAdapter = TaskAdapter(this, tasks)
        rv_mainInfo_add.adapter = taskAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun attemptGetTasks() {
        val dialogLogin = DialogUIUtils.showLoading(
            _context, "获取任务列表...", false, true,
            false,
            false
        )
        dialogLogin.show()
        try {

            var sql = "SELECT * FROM V_APPLY "
            val labNo = SpValueUtil.getString("LAB_NO", "")

            if (labNo.isNotEmpty()) {
                sql += if (labNo == "-1") {
                    " WHERE 1=1"
                } else {
                    " WHERE LAB_NO='$labNo'"
                }
            }

            RetrofitService.getApiService()
                .getTasks(sql)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Tasks> {
                    override fun onNext(t: Tasks) {
                        Log.i("Login", "onNext")
                        Log.v("total", t.total.toString())
                        taskAdapter?.changList(t)
                        Snackbar.make(
                            rv_mainInfo_add, "更新成功",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null)
                            .show()

                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.i("Login", "onSubscribe")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("Login", e.message.toString())
                    }

                    override fun onComplete() {
                        Log.i("Login", "onComplete")
                    }

                })
            DialogUIUtils.dismiss(dialogLogin)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /*设置监听器*/
    private fun setListener() {
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    val builder =
                        androidx.appcompat.app.AlertDialog.Builder(this@MainActivity).apply {
                            setTitle("提示")
                            setMessage("确定退出？")
                            setPositiveButton("确定") { _, _ ->
/*                            SpUtilKt.setBoolean(MyConfig.IS_LOGIN, false)
                            SpUtilKt.removeByKey(MyConfig.COOKIE)*/
                                val intentLogin = Intent()
                                intentLogin.setClass(
                                    this@MainActivity,
                                    LoginActivity::class.java
                                )
                                intentLogin.putExtra("login_type", -1)
                                startActivity(intentLogin)
                            }
                            setNegativeButton("取消", null)
                        }
                    builder.create().show()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                attemptGetTasks()
                /*val intentDetails = Intent()
                intentDetails.setClass(
                    this@MainActivity,
                    DetailsActivity::class.java
                )
                when (tv_title_main.text) {
*//*                    resources.getString(R.string.menu_sampling_type1) -> {
                        intentDetails.putExtra("fragment_type", 1)
                    }*//*
                }
                startActivity(intentDetails)*/
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 拦截返回事件，自处理
     */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                // Object mHelperUtils;
                Snackbar.make(
                    toolbar_main, "再按一次退出",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                    .show()
                mExitTime = System.currentTimeMillis()
            } else {
                ActivityUtil.closeAllActivity()
            }
        }
        //super.onBackPressed()
    }
}