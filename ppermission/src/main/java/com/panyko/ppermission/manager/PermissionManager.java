package com.panyko.ppermission.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.panyko.ppermission.callback.PermissionCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 权限管理类
 */
public class PermissionManager {
    private static PermissionManager instance;
    public int requestCode;
    private PermissionCallback permissionCallback;
    private Context context;

    public static PermissionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PermissionManager.class) {
                if (instance == null) {
                    instance = new PermissionManager(context);
                }
            }
        }
        return instance;
    }

    public PermissionManager(Context context) {
        requestCode = 0x001;
        this.context = context;
    }

    public boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 申请权限
     *
     * @param activity           页面
     * @param permissionCallback 回调接口
     * @param permissions        需要的权限名称
     */
    public void requestPermission(Activity activity, PermissionCallback permissionCallback, String... permissions) {
        this.permissionCallback = permissionCallback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> deniedPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permission);
                }
            }
            if (deniedPermissionList.size() > 0) {
                activity.requestPermissions(deniedPermissionList.toArray(new String[0]), requestCode);
            } else {
                if (permissionCallback != null) {
                    List<String> permissionList = Arrays.asList(permissions);
                    permissionCallback.onGrantedPermissions(permissionList);
                }
            }
        } else {
            // Android6.0以下，无需申请权限
            if (permissionCallback != null) {
                List<String> permissionList = Arrays.asList(permissions);
                permissionCallback.onGrantedPermissions(permissionList);
            }

        }

    }

    /**
     * 申请权限
     *
     * @param activity           页面
     * @param permissionCallback 回调接口
     * @param permissions        需要申请的权限集合
     */
    public void requestPermission(Activity activity, PermissionCallback permissionCallback, List<String> permissions) {
        this.permissionCallback = permissionCallback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> deniedPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permission);
                }
            }
            if (deniedPermissionList.size() > 0) {
                activity.requestPermissions(deniedPermissionList.toArray(new String[0]), requestCode);
            } else {
                if (permissionCallback != null) {
                    permissionCallback.onGrantedPermissions(permissions);
                }
            }
        } else {
            // Android6.0以下，无需申请权限
            if (permissionCallback != null) {
                permissionCallback.onGrantedPermissions(permissions);
            }
        }

    }

    /**
     * 请求结果
     *
     * @param activity     页面
     * @param requestCode  请求code
     * @param permissions  权限组
     * @param grantResults 结果组
     */
    public void requestResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == this.requestCode) {
            List<String> grantedList = new ArrayList<>();
            List<String> currentDeniedList = new ArrayList<>();
            List<String> foreverDeniedList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantedList.add(permissions[i]);
                } else {
                    boolean shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
                    if (shouldShowRequestPermissionRationale) {
                        currentDeniedList.add(permissions[i]);
                    } else {
                        foreverDeniedList.add(permissions[i]);
                    }
                }
            }
            if (permissionCallback != null) {
                permissionCallback.onGrantedPermissions(grantedList);
                permissionCallback.onCurrentDeniedPermissions(currentDeniedList);
                permissionCallback.onForeverDeniedAndPermissions(foreverDeniedList);
            }
        }
    }
}
