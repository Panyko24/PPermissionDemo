package com.panyko.ppermission.callback;

import java.util.List;

public interface PermissionCallback {
    /**
     * 通过的权限
     * @param grantedList 权限集合
     */
    void onGrantedPermissions(List<String> grantedList);

    /**
     * 当前拒绝的权限，下次依然可以申请
     * @param deniedList 权限集合
     */
    void onCurrentDeniedPermissions(List<String> deniedList);

    /**
     * 永远拒绝的权限
     * @param deniedList 权限集合
     */
    void onForeverDeniedAndPermissions(List<String> deniedList);
}
