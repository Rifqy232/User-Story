package com.mry.userstory.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.mry.userstory.R

class CustomEditTextEmail : AppCompatEditText, View.OnTouchListener {
    private lateinit var errorIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        errorIcon = ContextCompat.getDrawable(context, R.drawable.baseline_error_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                val isValid = isValidEmail(email)
                if (!isValid) {
                    setError(resources.getString(R.string.email_invalid), errorIcon)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, errorIcon, null)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}