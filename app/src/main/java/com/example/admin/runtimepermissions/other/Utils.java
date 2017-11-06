package com.example.admin.runtimepermissions.other;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.example.admin.runtimepermissions.R;

/**
 * Created by Admin on 20/09/2017.
 */

public class Utils
{
    private static String mTAG = "Utils";

    public static boolean isSelfPermissionGranted(String[] permissions, Context context)
    {
        boolean result = true;

        int target_sdk_version = Build.VERSION.SDK_INT;
        Log.v(mTAG, "target sdk version : " + target_sdk_version);

        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            target_sdk_version = packageInfo.applicationInfo.targetSdkVersion;
            Log.v(mTAG, "target sdk version : " + target_sdk_version);
        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(target_sdk_version >= Build.VERSION_CODES.M)
            {
                // if targetVersion > M, we use context.checkSelfPermission
                for(String permission : permissions)
                {
                    result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

                    if(!result)
                        break;
                }
            }
            else
            {
                // if targetVersion < M, we use PermissionChecker.checkSelfPermission
                for(String permission : permissions)
                {
                    result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;

                    if(!result)
                        break;
                }
            }
        }

        return result;
    }

    public static String[] getPermissions()
    {
        String[] permissions = new String[]
                {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                };

                return permissions;
    }

    public static AlertDialog.Builder getAlertDialog(Context context, String title, String message)
    {
        AlertDialog.Builder alertDialog = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        else
            alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        return alertDialog;
    }

}
