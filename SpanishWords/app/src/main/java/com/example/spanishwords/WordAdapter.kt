package com.example.spanishwords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class WordAdapter (context: Context,
                   var items: MutableList<Word>) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    // 一覧内で一意になるやつ
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: createView(parent)
        val item = getItem(position)
        val viewHolder = view.tag as ViewHolder
        viewHolder.text.text = item.spanish
        viewHolder.checkBox.isChecked = item.isChecked
        return view
    }

    private fun createView(parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.row_word, parent, false)
        view.tag = ViewHolder(view)
        return view
    }

    private class ViewHolder(view: View) {
        val text = view.findViewById<TextView>(R.id.text)!!
        val checkBox = view.findViewById<CheckBox>(R.id.check_box)!!
    }
}