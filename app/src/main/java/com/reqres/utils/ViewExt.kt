package com.reqres.utils

import android.app.Service
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.reqres.R

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.decrement()
            }
        })
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> {
                    hideKeyboard()
                    showSnackbar(it, timeLength)
                }
                is Int -> {
                    hideKeyboard()
                    showSnackbar(this.context.getString(it), timeLength)
                }
                else -> {
                }
            }

        }
    })
}

fun View.showToast(
    lifecycleOwner: LifecycleOwner,
    ToastEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {

    ToastEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Toast.makeText(this.context, it, timeLength).show()
                is Int -> Toast.makeText(this.context, this.context.getString(it), timeLength).show()
                else -> {
                }
            }
        }
    })
}

fun RecyclerView.addOnScrolledToEnd(onScrolledToEnd: () -> Unit) {

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        private var loading = true
        private var previousTotal = 0

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            with(layoutManager as LinearLayoutManager) {
                val totalItemCount = itemCount
                val lastVisibleItem = findLastVisibleItemPosition() + 1

                if (loading && totalItemCount != previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }

                if (!loading && lastVisibleItem == totalItemCount) {
                    onScrolledToEnd()
                    loading = true
                }
            }
        }
    })
}
