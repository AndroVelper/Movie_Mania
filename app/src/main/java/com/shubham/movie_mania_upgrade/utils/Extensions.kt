package com.shubham.movie_mania_upgrade.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun Any.exceptionHandler(task: () -> Unit) {
    try {
        task()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.closeKeyBoard() {
    val keyboardAccess =
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    keyboardAccess.hideSoftInputFromWindow(view?.windowToken, 0)
}


/**
 * This will run the task in the async manner using the coroutine scope
 * by Default this will use the Dispatcher.IO
 * @sample : Best used for the suspend function with background syncing
 * @param dispatcher : on Which threadPool you need to run the task
 * @param task : task when need to be implemented in the code.
 *
 * **/
fun Fragment.runOnBackgroundThread(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    task: suspend () -> Unit
) {
    CoroutineScope(dispatcher).launch {
        task()
    }
}

/**
 * This will run the task in the async manner using the coroutine scope
 * by Default this will use the Dispatcher.Main
 * @sample :  Best used for the task that will run on the main thread and does not
 * work with suspend calls
 * @param dispatcher : on Which threadPool you need to run the task
 * @param task : task when need to be implemented in the code.
 * **/
fun Fragment.runOnMainThread(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    task: () -> Unit
) {
    CoroutineScope(dispatcher).launch {
        task()
    }
}


/**
 * This will be used to show the view in the screen
 * @sample :  Best used for the task which is used to show the view the view on the screen
 * **/
fun View.showView() {
    this.visibility = View.VISIBLE
}

/**
 * This will be used to hide the view in the screen
 * @sample :  Best used for the task which is used to hide the view the view on the screen
 * **/
fun View.hideView() {
    this.visibility = View.GONE
}


/**
 * This will be used to show the toast using the string prebuild class
 * @return show the toast on the screen
 * @sample : Toast will be shown on the screen
 *
 * @param context : Ref of the screen wrt we need to show the toast
 * @param length : Toast.LENGTH_SHORT by default the length is short but you can change this
 * Toast.Length_LONG for 5s time interval
 * **/
fun String.showToast(context: Context , length : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, length).show()
}
