package com.shubham.lib_permission.communicator


/**
 * This interface will provide me the callbacks for the permission Denied Callbacks when user want to
 * use this when you want to handle the permission callbacks from own which help you to write your own
 * implementation
 *
 * @see onPermissionDenied when user has chosen don;t allow for the permission or the permission dialog has been seen two
 * times on the screen now user has to move to setting for this.
 *
 * @see onPermissionGranted when user has granted the permission to app.
 *
 * @see requestForPermission : when we want to show the permission request dialog from own.
 **/
interface IPermissionCallbackProvider {
    fun onPermissionGranted()
    fun onPermissionDenied()
    fun requestForPermission()
    fun onPermissionDialogDismissed()

}