package com.patel.gomap.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.github.aakira.expandablelayout.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patel.gomap.R
import com.patel.gomap.model.PinData

class RecyclerViewAdapter (con: Context, result: List<PinData>) : RecyclerView.Adapter<RecyclerViewAdapter.viewHolder>()  {

    private lateinit var v: View
    private lateinit var listener: OnItemClickListener
    private var cons:Context = con
    private var list : List<PinData> = result
    private var expandState : SparseBooleanArray = SparseBooleanArray()
    init{
        for(i in list.indices){
            expandState.append(i,false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        v = LayoutInflater.from(cons).inflate(R.layout.row, parent, false)
        return viewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val location : PinData = list[position]
        holder.setIsRecyclable(false)
        holder.locationName.text = location.name
        holder.expandable.setInRecyclerView(true)
        holder.expandable.isExpanded = expandState.get(position)
        holder.description.text = location.despcription
        val layout  = object : ExpandableLayoutListenerAdapter(){

            override fun onPreOpen() {
                rotater(holder.drop, 0f, 180f).start()
                expandState.put(position, true)
            }

            override fun onPreClose() {
                rotater(holder.drop, 180f, 0f).start()
                expandState.put(position, false)
            }
        }
        holder.expandable.setListener(layout)
        if(expandState[position]){
            holder.drop.rotation = 180f
        }else{
            holder.drop.rotation = 0f
        }
        holder.drop.setOnClickListener{
            holder.expandable.toggle()
        }
    }

    private fun rotater(drop: RelativeLayout, from: Float, end: Float): ObjectAnimator {
        val animate : ObjectAnimator = ObjectAnimator.ofFloat(drop,"rotation",from, end)
        animate.duration = 300
        animate.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        return animate

    }

    inner class viewHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener{
        var locationName: TextView
        var drop : RelativeLayout
        var description : TextView
        var expandable : ExpandableLinearLayout
        init {
            drop= item.findViewById(R.id.drop)
            locationName= item.findViewById(R.id.locationName)
            item.setOnClickListener(this)
            description = item.findViewById(R.id.description)
            expandable= item.findViewById(R.id.expand)
        }
        override fun onClick(v: View?) {
            if(listener != null){
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    listener.itemClick(position)
                }
            }
        }
    }

    interface OnItemClickListener{
        fun itemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}