package com.example.carrent.ClassProject

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class adapterSearch(private val context: Context, private var suggestions: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, suggestions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        // Здесь можно изменить внешний вид элемента списка, например, цвет текста
        val textView = view.findViewById<TextView>(android.R.id.text1)
        //textView?.setTextColor(ContextCompat.getColor(context, R.color.your_text_color)) //R.color.your_text_color - ваш цвет
        return view
    }

    fun updateSuggestions(newSuggestions: List<String>) {
        suggestions = newSuggestions
        notifyDataSetChanged()
    }
}

