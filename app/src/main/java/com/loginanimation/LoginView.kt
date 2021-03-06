package com.loginanimation

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.login_view.view.*
import com.devs.vectorchildfinder.VectorChildFinder
import com.loginanimation.YetStates.STATE_FOCUS
import com.loginanimation.YetStates.STATE_INITIAL
import com.loginanimation.YetStates.STATE_SMILE
import com.loginanimation.YetStates.STATE_LOOKING
import com.loginanimation.YetStates.STATE_NEUTRAL

/**
 * Created by rafaela.araujo on 02/04/18.
 */
class LoginView(context: Context, attr: AttributeSet? = null) : FrameLayout(context, attr), TextWatcher {

    private lateinit var vector: VectorChildFinder

    init {
        LayoutInflater.from(context).inflate(R.layout.login_view, this)
        initViewActions()
    }

    private fun initViewActions() {
        changeImageState(STATE_INITIAL)

        rootView.setOnClickListener {
            returnToInitialState()
        }

        edit_text_password.setOnFocusChangeListener { _, b ->
            if (b) {
                image.setImageDrawable(context.getDrawable(R.drawable.asl_yet))
                changeImageState(STATE_LOOKING)
            } else
                changeImageState(STATE_INITIAL)

        }

        edit_text_email.setOnFocusChangeListener { _, b ->
            if (b) {
                changeImageState(STATE_FOCUS)
            } else {
                changeImageState(STATE_INITIAL)
            }
        }

        edit_text_email.addTextChangedListener(this)
    }

    private fun returnToInitialState() {
        changeImageState(STATE_INITIAL)
        edit_text_password.clearFocus()
        edit_text_email.clearFocus()
        hideSoftKeyboard()
    }

    private fun updateFaceView(charSequenceSize: Int, before: Int) {
        if (charSequenceSize == 1 && before == 0) {
            changeImageState(STATE_SMILE)

        } else if (charSequenceSize <= 30) {
            changeVectorParameters(charSequenceSize)
        }
    }

    private fun changeVectorParameters(charSequenceSize: Int) {
        vector = if (charSequenceSize > 0) {
            VectorChildFinder(context, R.drawable.vd_yet_email_smile, image)
        } else {

            VectorChildFinder(context, R.drawable.vd_yet_email, image)
        }

        val face = vector.findGroupByName("face")
        val group7 = vector.findGroupByName("group_7")
        val group6 = vector.findGroupByName("group_6")
        val group3 = vector.findGroupByName("group_3")
        val group4 = vector.findGroupByName("group_4")
        val group1 = vector.findGroupByName("group_1")

        group7.rotation = group7.rotation.minus((charSequenceSize * .2)).toFloat()
        group7.translateY = group7.translateY.plus((charSequenceSize * .5)).toFloat()
        group6.rotation = group6.rotation.minus((charSequenceSize * .1)).toFloat()
        group3.rotation = group3.rotation.minus((charSequenceSize * .1)).toFloat()

        group6.translateY = group6.translateY.plus((charSequenceSize * .2)).toFloat()
        group3.translateY = group3.translateY.plus((charSequenceSize * .2)).toFloat()

        group4.translateX = group4.translateX.minus((charSequenceSize * .2)).toFloat()
        group1.translateX = group1.translateX.minus((charSequenceSize * .2)).toFloat()
        face.translateX = face.translateX.plus(charSequenceSize * .5).toFloat()
        face.translateY = face.translateY.plus(charSequenceSize * .2).toFloat()
    }

    private fun changeImageState(state: IntArray) {
        image.setImageState(state, true)
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
        if (s.isNotEmpty()) {
            updateFaceView(s.length, before)
        } else {
            updateFaceView(0, 0)
        }
    }

}