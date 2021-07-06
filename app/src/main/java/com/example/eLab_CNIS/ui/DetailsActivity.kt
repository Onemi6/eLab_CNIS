package com.example.eLab_CNIS.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.eLab_CNIS.R
import com.example.eLab_CNIS.adapter.MyViewPagerAdapter
import com.example.eLab_CNIS.adapter.TaskAdapter
import com.example.eLab_CNIS.models.SamplingInfo
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private var fragmentType = 0
    private lateinit var context: Context
    private lateinit var samplingInfoRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private var samplingInfoList: MutableList<SamplingInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        context = this
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        val intent = intent
        fragmentType = intent.getIntExtra("fragment_type", 0)
        //Log.v("fragment_type", fragmentType.toString())
        toolbar_details.title = ""
        setSupportActionBar(toolbar_details)
        /*显示Home图标*/
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        /*设置ToolBar标题，使用TestView显示*/
        tv_title_details.text = resources.getString(R.string.title_details)
        initFragments()
        when (fragmentType) {
            1 -> {
                view_pager.currentItem = 0
            }
            2 -> {
                view_pager.currentItem = 1
            }
            3 -> {
                view_pager.currentItem = 2
            }
            4 -> {
                view_pager.currentItem = 3
            }
            5 -> {
                view_pager.currentItem = 4
            }
            6 -> {
                view_pager.currentItem = 5
            }
            7 -> {
                view_pager.currentItem = 6
            }
        }
    }

    /**
     * 初始化Fragment
     */
    private fun initFragments() {
        val viewPagerAdapter = MyViewPagerAdapter(supportFragmentManager).apply {
        }
        view_pager.offscreenPageLimit = 1
        view_pager.adapter = viewPagerAdapter
    }

    //后期每次只加载一个Fragment，考虑使用父类的变量
    fun getSamplingInfoRecyclerView(): RecyclerView {
        return this.samplingInfoRecyclerView
    }

    fun getSamplingInfoList(): MutableList<SamplingInfo> {
        return this.samplingInfoList
    }

    fun getSamplingInfoAdapter(): TaskAdapter {
        return this.taskAdapter
    }
}