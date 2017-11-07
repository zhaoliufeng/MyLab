package me.zhaoliufeng.mylab;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaol on 2017/9/15.
 */

public class PermissionsActivity extends Activity {
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        mPermissionsChecker = new PermissionsChecker(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(PERMISSIONS); // 系统版本大于M 请求权限
    }

    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    public void checkOnClick(View view) {
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
        builder.setTitle("提示");
        builder.setMessage("缺失权限");

        // 拒绝, 退出应用
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                startActivity(intent);
            }
        });

        builder.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TAG", "onRestart");

    }


    private void requestPermissions(String... permissions) {
       //
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            Toast.makeText(this, "缺少权限", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this, filterPermissions(PERMISSIONS), 0);
           // showMissingPermissionDialog();
        } else {
            Toast.makeText(this, "以获取全部权限", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] filterPermissions(String... permissions){
        List<String> per = new ArrayList<>();
        for (String permission : permissions){
            if (mPermissionsChecker.lacksPermission(permission))
                per.add(permission);
        }
        String[] res = new String[per.size()];
        int index = 0;
        for (String str : per){
            res[index++] = str;
        }
        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
        finish();
    }

    //检查权限
    public class PermissionsChecker {
        private final Context mContext;

        public PermissionsChecker(Context context) {
            mContext = context.getApplicationContext();
        }

        // 判断权限集合
        public boolean lacksPermissions(String... permissions) {
            for (String permission : permissions) {
                if (lacksPermission(permission)) {
                    return true;
                }
            }
            return false;
        }

        // 判断是否缺少权限
        private boolean lacksPermission(String permission) {
            return ContextCompat.checkSelfPermission(mContext, permission) ==
                    PackageManager.PERMISSION_DENIED;
        }
    }
}
