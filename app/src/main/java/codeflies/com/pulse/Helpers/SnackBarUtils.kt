package codeflies.com.pulse.Helpers

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar

class SnackBarUtils {

    companion object {
        fun showTopSnackbar(context: Context, message: String, color: Int) {
            val snackbar = Snackbar.make(
                (context as Activity).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG
            )
            snackbar.setBackgroundTint(color)

            // Get the Snackbar's view
            val snackbarView = snackbar.view

            // Set the gravity to top
            val params = snackbarView.layoutParams as FrameLayout.LayoutParams

            params.gravity = Gravity.TOP
            snackbarView.layoutParams = params

            // Set custom margins if needed
            params.topMargin = 100 // Adjust as needed

            // Update the layout parameters
            snackbar.show()
        }
    }
}