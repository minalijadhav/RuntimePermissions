package com.example.admin.runtimepermissions.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.admin.runtimepermissions.R;
import com.example.admin.runtimepermissions.other.Constants;
import com.example.admin.runtimepermissions.other.Utils;

public class MainActivity extends AppCompatActivity
{
    private String mTAG = "MainActivity";

    private int current_api_version = 0;
    private AlertDialog.Builder mAlertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting Current API Version
        current_api_version = Build.VERSION.SDK_INT;
        Log.v(mTAG, "current api version : " + current_api_version);

        // Checking Runtime Permissions
        checkPermissionsIfNeeded();
    }

    private void enableSettings()
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, Constants.PERMISSIONS_REQUEST_CODE);
    }

    private void checkPermissionsIfNeeded()
    {
        if(current_api_version >= 23)
        {
            if(!Utils.isSelfPermissionGranted(Utils.getPermissions(), MainActivity.this))
            {
                ActivityCompat.requestPermissions(MainActivity.this, Utils.getPermissions(), Constants.PERMISSIONS_REQUEST_CODE);
            }
            else
            {
                Log.v(mTAG, "permissions already granted");
            }
        }
        else
        {
            Log.v(mTAG, "no need to check permissions");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case Constants.PERMISSIONS_REQUEST_CODE :
                // Checking Runtime Permissions
                checkPermissionsIfNeeded();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case Constants.PERMISSIONS_REQUEST_CODE :
                try
                {
                    for(int i = 0; i < grantResults.length; i++)
                    {
                        if(grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        {
                            Log.v(mTAG, "permission granted");
                        }
                        else
                        {
                            Log.v(mTAG, "permission denied");

                            boolean show_rationale = shouldShowRequestPermissionRationale(permissions[i]);
                            Log.v(mTAG, "show rationale : " + show_rationale);

                            if(!show_rationale)
                            {
                                mAlertDialog = Utils.getAlertDialog(MainActivity.this, "", "Please allow the permissions to continue");
                                mAlertDialog.setPositiveButton("SETTINGS", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, int i)
                                    {
                                        new Handler().postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                // Dismissing an alert dialog
                                                dialogInterface.dismiss();

                                                // Passing intent to Settings to enable permissions
                                                enableSettings();
                                            }
                                        }, 100);

                                    }
                                });
                                mAlertDialog.show();

                            }
                            else
                            {
                                mAlertDialog = Utils.getAlertDialog(MainActivity.this, "", "Please select \"ALLOW\" to continue");
                                mAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, int i)
                                    {
                                        new Handler().postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                // Dismissing an alert dialog
                                                dialogInterface.dismiss();

                                                // Checking Runtime Permissions
                                                checkPermissionsIfNeeded();
                                            }
                                        }, 100);
                                    }
                                });
                                mAlertDialog.show();
                            }
                        }
                        return;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }
}
