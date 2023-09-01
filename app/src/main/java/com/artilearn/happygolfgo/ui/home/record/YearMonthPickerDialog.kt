package com.artilearn.happygolfgo.ui.home.record

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.DialogFragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.exam.YearMonth
import java.util.*

class YearMonthPickerDialog<Button : View?>(
    val examHistory: List<YearMonth>
) : DialogFragment() {
    private var listener: SelectDateListener? = null
    private val MAX_YEAR = 2099
    private val MIN_YEAR = 1980

    val yearToMonthsMap: Map<String, List<String>> by lazy {
        examHistory.groupBy(
            { it.yearMonth.substring(0, 4) },  // 년도 추출
            { it.yearMonth.substring(4, 6) }   // 월 추출
        )
    }
    val uniqueYears: List<Int> by lazy {
        examHistory.map { it.yearMonth.substring(0, 4).toInt() }.distinct().sorted()
    }

    var cal = Calendar.getInstance()
    fun setListener(listener: SelectDateListener) {
        this.listener = listener
    }

    var btnConfirm: Button? = null
    var btnCancel: Button? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val dialog: View = inflater.inflate(R.layout.year_month_picker, null).also {
            btnConfirm = it.findViewById<Button>(R.id.btn_confirm)
            btnCancel = it.findViewById<Button>(R.id.btn_cancel)
        }

        val monthPicker =
            dialog.findViewById<View>(R.id.picker_month) as NumberPicker
        val yearPicker =
            dialog.findViewById<View>(R.id.picker_year) as NumberPicker
        monthPicker.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        yearPicker.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        btnConfirm?.setOnClickListener {
            val month = monthPicker.displayedValues[monthPicker.value-1]
            listener?.onSelectDate(Pair(yearPicker.value.toString(), month))
            dismiss()
        }
        btnCancel?.setOnClickListener {
            dismiss()
        }

        yearPicker.minValue = uniqueYears.minOrNull() ?: MIN_YEAR
        yearPicker.maxValue = uniqueYears.maxOrNull() ?: MAX_YEAR
        yearPicker.displayedValues = uniqueYears.map { it.toString() }.toTypedArray()
        builder.setView(dialog)

        yearPicker.setOnValueChangedListener { _, _, newVal ->
            val availableMonths = yearToMonthsMap[newVal.toString()] ?: emptyList()
            setMonthPickerValues(availableMonths, monthPicker)
        }

        setInitialMonths(yearPicker.value.toString(), monthPicker)

        return builder.create()
    }

    // 초기 월 설정
    private fun setInitialMonths(year: String, monthPicker: NumberPicker) {
        val availableMonths = yearToMonthsMap[year] ?: emptyList()
        setMonthPickerValues(availableMonths, monthPicker)
    }

    // monthPicker에 사용할 월 설정
    private fun setMonthPickerValues(availableMonths: List<String>, monthPicker: NumberPicker) {
        if(availableMonths.isNotEmpty()) {
            monthPicker.displayedValues = null  // 이전 displayedValues를 초기화
            monthPicker.minValue = 1
            monthPicker.maxValue = availableMonths.size
            monthPicker.displayedValues = availableMonths.toTypedArray()

            // 현재 선택된 값이 새로운 범위를 벗어나면 첫 번째 값으로 설정
            if (monthPicker.value > availableMonths.size || monthPicker.value < 1) {
                monthPicker.value = 1
            }
        }
    }

    interface SelectDateListener {
        fun onSelectDate(yearMonth: Pair<String, String>)
    }
}