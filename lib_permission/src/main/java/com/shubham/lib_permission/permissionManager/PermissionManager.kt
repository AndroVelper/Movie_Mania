package com.shubham.lib_permission.permissionManager

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.shubham.lib_permission.communicator.IPermissionCallbackProvider
import com.shubham.lib_permission.databinding.ItemDialogPermissionBinding


/**
 * This is a permission manager work in two cases
 * case 1 : when user want to handle the permission Callbacks from own
 * then use the Secondary constructor
 *
 * case 2 : when user want this library to handle the permission cases by own.
 * then use the primary constructor
 * **/

class PermissionManager() {
    private var permissionCallback: IPermissionCallbackProvider? = null

    /**
     * @see IPermissionCallbackProvider for the callbacks
     * @see IPermissionCallbackProvider.onPermissionGranted for the callback for the permission granted
     * @see IPermissionCallbackProvider.onPermissionDenied for the callback for the permission denied.
     * @see IPermissionCallbackProvider.requestForPermission when you want to show the custom Dialog for the permission for displaying the information before requesting the permission
     * **/
    fun setListener(listener: IPermissionCallbackProvider) {
        this.permissionCallback = listener
    }


    /**
     * This is to check the status of permission for our app.
     *
     * @param context : reference of the calling activity.
     * @param permissionName : name of the permission eg android.permission.INTERNET for internet permission
     * @return true when permission is granted otherwise false
     * **/

    private fun checkIsPermissionGranted(context: Context, permissionName: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permissionName
        ) == PermissionChecker.PERMISSION_GRANTED
    }


    /**
     * This is to check the whether we can show the rationale for the permission.
     *
     * @param activity : reference of the calling activity.
     * @param permissionName : name of the permission eg android.permission.INTERNET for internet permission
     * @return true if we can show the rationale
     * **/

    private fun checkCanWeShowRationale(activity: Activity, permissionName: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName)
    }


    /***
     *  Request for the permission if granted return true otherwise request for the permission
     *
     *  @param activity : Reference of the parent activity
     *  @param context : context of the parent Activity
     *  @param permissionName : permission String that we need to request for the app
     *  @param permissionMessage : Message to be shown in the App
     *
     *  @return return true when the permission is granted otherWise false.
     *  **/

    fun requestPermission(
        activity: Activity,
        context: Context,
        permissionDialogHeading: String?,
        permissionName: String,
        permissionMessage: String?,
        requestCode: Int = 1000,
        launcher: ActivityResultLauncher<String>? = null
    ): Boolean {
        return when {
            // Permission is already granted
            checkIsPermissionGranted(context, permissionName) -> {
                permissionCallback?.onPermissionGranted() ?: true
                true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permissionName
            ) -> {
                showRationaleAndRequestPermission(context, permissionMessage, permissionName)
                false
            }

            else -> {
                requestForPermission(activity, permissionString = permissionName, requestCode)
                permissionCallback?.onPermissionDenied()
                false
            }
        }
    }


    /**
     * Just override the permission using the @see onRequestPermissionsResult method
     * **/

    private fun requestForPermission(
        activity: Activity,
        permissionString: String,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permissionString),
            requestCode // Example request code
        )

    }

    private fun showRationaleAndRequestPermission(
        context: Context,
        permissionMessage: String?,
        permissionDialogHeading: String?
    ) {
        val alertDialog = Dialog(context)
        val dialogBinding: ItemDialogPermissionBinding = ItemDialogPermissionBinding.inflate(
            LayoutInflater.from(context) , null , false)
        alertDialog.setContentView(dialogBinding.root)
        alertDialog.setCancelable(false)
        val window = alertDialog.window
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% of screen width
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        permissionMessage?.let {
            dialogBinding.permissionDescription.text = it
        }
        permissionDialogHeading?.let {
            dialogBinding.permissionHeading.text = it
        }
        dialogBinding.okButton.setOnClickListener {
            openSettings(context)
        }
        dialogBinding.cancelButton.setOnClickListener {
            permissionCallback?.onPermissionDenied()
            alertDialog.dismiss()
        }
        alertDialog.setOnDismissListener {
            permissionCallback?.onPermissionDialogDismissed()
        }
        alertDialog.show()
    }


    /**
     * Open the setting of the particular app
     * **/
    private fun openSettings(context: Context) {
        val settingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(settingIntent)
    }


}