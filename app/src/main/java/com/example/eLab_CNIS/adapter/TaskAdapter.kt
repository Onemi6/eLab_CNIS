package com.example.eLab_CNIS.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.eLab_CNIS.R
import com.example.eLab_CNIS.models.Apply
import com.example.eLab_CNIS.models.Tasks

class TaskAdapter(
    context: Context?,
    private var tasks: Tasks?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener,
    View.OnLongClickListener {

    private val viewType = -1
    private var mContext: Context? = context
    private var mOnClickListener: OnClickListener? = null
    private var mOnLongClickListener: OnLongClickListener? = null
    private val pos = intArrayOf(-1, -1)
    private var defItem: Int = -1
    private var apply: Apply? = null


    //加载item 的布局  创建ViewHolder实例
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val emptyView: View =
            LayoutInflater.from(mContext).inflate(R.layout.rv_empty, parent, false)
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false)
        if (this.viewType == viewType) {
            return EmptyViewHolder(emptyView)
        }
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return ViewHolder(view)
    }

    //对RecyclerView子项数据进行赋值
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            apply = tasks?.data?.get(position)

            holder.taskNum.text = (position + 1).toString()
            holder.taskNo.setText(apply?.NO)
            holder.taskGoodsName.setText(apply?.GOODS_NAME)
            holder.taskApplyKind.setText(apply?.APPLY_KIND)
            holder.taskGoodsType.setText(apply?.GOODS_TYPE)
            holder.taskRecorder.setText(apply?.RECORDER)
        }
}

//返回子项个数
override fun getItemCount(): Int {
    //获取传入adapter的条目数，没有则返回 1
    return if (tasks?.data?.isNotEmpty() == true) tasks?.data!!.size else -1
}

override fun getItemViewType(position: Int): Int {
    return if (tasks?.data.isNullOrEmpty()) {
        viewType
    } else super.getItemViewType(position)
}

fun getItem(position: Int): Apply? {
    this.defItem = position;
    notifyDataSetChanged();
    return tasks?.data?.get(position);
}

fun removeItem(position: Int) {
    this.tasks?.data?.removeAt(position)
    notifyDataSetChanged()
}

fun changList(tasks: Tasks?) {
    //this.samplingInfoList.clear();
    if (tasks != null) {
        this.tasks?.data = tasks.data
    }
    notifyDataSetChanged()
}

fun Refresh_item(position: Int) {
    pos[1] = pos[0]
    pos[0] = position
    notifyItemChanged(position)
}

fun Refresh_all() {
    pos[1] = -1
    pos[0] = -1
    notifyDataSetChanged()
}

fun setOnClickListener(listener: OnClickListener?) {
    mOnClickListener = listener
}

override fun onClick(view: View) {
    if (null != mOnClickListener) {
        mOnClickListener!!.onClick(view, view.tag as Int) //getTag()获取数据
    }
}

fun setOnLongClickListener(listener: OnLongClickListener?) {
    mOnLongClickListener = listener
}

override fun onLongClick(view: View): Boolean {
    if (null != mOnLongClickListener) {
        mOnLongClickListener!!.onLongClick(view, view.tag as Int)
    }
    // 消耗事件，否则长按逻辑执行完成后还会进入点击事件的逻辑处理
    return true
}

/**
 * 手动添加点击事件
 */
interface OnClickListener {
    fun onClick(view: View?, position: Int)
}

/**
 * 手动添加长按事件
 */
interface OnLongClickListener {
    fun onLongClick(view: View?, position: Int)
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val taskNum: TextView = view.findViewById(R.id.task_num)
    val taskNo: EditText = view.findViewById(R.id.task_NO)
    val taskGoodsName: EditText = view.findViewById(R.id.task_GOODS_NAME)
    val taskApplyKind: EditText = view.findViewById(R.id.task_APPLY_KIND)
    val taskGoodsType: EditText = view.findViewById(R.id.task_GOODS_TYPE)
    val taskRecorder: EditText = view.findViewById(R.id.task_RECORDER)
}

class EmptyViewHolder  //private TextView mEmptyTextView;
    (view: View?) : RecyclerView.ViewHolder(view!!)
}