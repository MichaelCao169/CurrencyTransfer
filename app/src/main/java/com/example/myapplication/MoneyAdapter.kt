package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MoneyAdapter(
    context: Context,
    private val currencies: List<MoneyClass>
) : ArrayAdapter<MoneyClass>(context, android.R.layout.simple_spinner_item, currencies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val currency = currencies[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = currency.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
