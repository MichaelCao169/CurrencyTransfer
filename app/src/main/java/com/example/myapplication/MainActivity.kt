package com.example.myapplication

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var isInputFirstCurrency = true
    private lateinit var listMoney: MutableList<MoneyClass>
    private lateinit var spinnerFirst: Spinner
    private lateinit var spinnerSecond: Spinner
    private lateinit var moneyFirst: TextView
    private lateinit var moneySecond: TextView
    private var stringFirst = ""
    private var stringSecond = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        moneyFirst = findViewById(R.id.moneyFirst)
        moneySecond = findViewById(R.id.moneySecond)
        moneyFirst.setTypeface(null, Typeface.BOLD_ITALIC)

        setupCurrencies()
        setupSpinners()
        setupButtons()
    }

    private fun setupCurrencies() {
        listMoney = mutableListOf(
            MoneyClass("VietNam - Dong", "VND", 25294f),
            MoneyClass("United Kingdom - Pound", "£", 0.80f),
            MoneyClass("Europe - Euro", "€", 0.91f),
            MoneyClass("Japan - Yen", "¥", 108.46f),
            MoneyClass("Korea - WON", "₩", 1211.05f),
            MoneyClass("United States - USD", "$", 1f)
        )
    }

    private fun setupSpinners() {
        val moneyAdapter = MoneyAdapter(this, listMoney)

        spinnerFirst = findViewById(R.id.spinnerFirst)
        spinnerFirst.adapter = moneyAdapter
        spinnerFirst.onItemSelectedListener = CurrencySelectionListener()

        spinnerSecond = findViewById(R.id.spinnerSecond)
        spinnerSecond.adapter = moneyAdapter
        spinnerSecond.onItemSelectedListener = CurrencySelectionListener()
    }

    private fun setupButtons() {
        val buttons = listOf(
            R.id.buttonC, R.id.buttonCE, R.id.buttonOne, R.id.buttonTwo,
            R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix,
            R.id.buttonSeven, R.id.buttonEight, R.id.buttonNine, R.id.buttonZero,
            R.id.moneyFirst, R.id.moneySecond
        )

        for (buttonId in buttons) {
            findViewById<View>(buttonId).setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.moneyFirst -> switchToFirstCurrency()
            R.id.moneySecond -> switchToSecondCurrency()
            R.id.buttonC -> clearLastDigit()
            R.id.buttonCE -> clearAll()
            else -> handleNumberInput(id)
        }
    }

    private fun handleNumberInput(id: Int) {
        val digit = when (id) {
            R.id.buttonOne -> "1"
            R.id.buttonTwo -> "2"
            R.id.buttonThree -> "3"
            R.id.buttonFour -> "4"
            R.id.buttonFive -> "5"
            R.id.buttonSix -> "6"
            R.id.buttonSeven -> "7"
            R.id.buttonEight -> "8"
            R.id.buttonNine -> "9"
            R.id.buttonZero -> "0"
            else -> return
        }

        if (isInputFirstCurrency) {
            stringFirst += digit
            updateConversion(stringFirst, true)
        } else {
            stringSecond += digit
            updateConversion(stringSecond, false)
        }
    }

    private fun updateConversion(input: String, isFirst: Boolean) {
        val currency1 = listMoney[spinnerFirst.selectedItemPosition]
        val currency2 = listMoney[spinnerSecond.selectedItemPosition]
        val conversionRate = if (isFirst) currency2.coverUSD / currency1.coverUSD else currency1.coverUSD / currency2.coverUSD

        val amount = input.toFloatOrNull() ?: 0f
        val result = amount * conversionRate
        val formattedResult = String.format("%.2f", result)

        if (isFirst) {
            moneyFirst.text = input
            moneySecond.text = formattedResult
        } else {
            moneySecond.text = input
            moneyFirst.text = formattedResult
        }
    }

    private fun switchToFirstCurrency() {
        isInputFirstCurrency = true
        moneyFirst.setTypeface(null, Typeface.BOLD_ITALIC)
        moneySecond.setTypeface(null, Typeface.NORMAL)
        clearAll()
    }

    private fun switchToSecondCurrency() {
        isInputFirstCurrency = false
        moneySecond.setTypeface(null, Typeface.BOLD_ITALIC)
        moneyFirst.setTypeface(null, Typeface.NORMAL)
        clearAll()
    }

    private fun clearLastDigit() {
        if (isInputFirstCurrency && stringFirst.isNotEmpty()) {
            stringFirst = stringFirst.dropLast(1)
            updateConversion(stringFirst, true)
        } else if (!isInputFirstCurrency && stringSecond.isNotEmpty()) {
            stringSecond = stringSecond.dropLast(1)
            updateConversion(stringSecond, false)
        }
    }

    private fun clearAll() {
        stringFirst = ""
        stringSecond = ""
        moneyFirst.text = "0"
        moneySecond.text = "0"
    }

    inner class CurrencySelectionListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            clearAll()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}
