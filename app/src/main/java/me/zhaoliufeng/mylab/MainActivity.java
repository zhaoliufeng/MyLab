package me.zhaoliufeng.mylab;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import me.zhaoliufeng.mylab.BootPage.BootPageActivity;
import me.zhaoliufeng.mylab.Chart.ChartActivity;
import me.zhaoliufeng.mylab.ColorPicker.ColorPickerActivity;
import me.zhaoliufeng.mylab.ColorPicker.RingColorPickerActivity;
import me.zhaoliufeng.mylab.CoutomClickView.MulitClickActivity;
import me.zhaoliufeng.mylab.CustomButton.SwitchButtonActivity;
import me.zhaoliufeng.mylab.CustomSeekBar.SeekBarActivity;
import me.zhaoliufeng.mylab.DashBoardView.DashBoardActivity;
import me.zhaoliufeng.mylab.DrawerPlusLayout.DrawerPlusActivity;
import me.zhaoliufeng.mylab.ItemMask.ItemMaskActivity;
import me.zhaoliufeng.mylab.NumTextView.NumTextViewActivity;
import me.zhaoliufeng.mylab.PayPwView.PayPwActivity;
import me.zhaoliufeng.mylab.lab.LabActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**数字增长动画控件*/
    public void numTextViewOnClick(View view) {
        startActivity(new Intent(this, NumTextViewActivity.class));
    }

    public void itemMaskOnClick(View view) {
        startActivity(new Intent(this, ItemMaskActivity.class));
    }

    public void labOnClick(View view) {
        startActivity(new Intent(this, LabActivity.class));
    }

    public void drawerPlusOnClick(View view) {
        startActivity(new Intent(this, DrawerPlusActivity.class));
    }

    public void bootPageOnClick(View view) {
        startActivity(new Intent(this, BootPageActivity.class));
    }

    public void payPwOnClick(View view) {
        startActivity(new Intent(this, PayPwActivity.class));
    }

    public void chartOnClick(View view) {
        startActivity(new Intent(this, ChartActivity.class));
    }

    public void boardOnClick(View view) {
        startActivity(new Intent(this, DashBoardActivity.class));
    }

    public void switchOnClick(View view) {
        startActivity(new Intent(this, SwitchButtonActivity.class));
    }

    public void colorOnClick(View view) {
        startActivity(new Intent(this, ColorPickerActivity.class));
    }

    public void seekOnClick(View view) {
        startActivity(new Intent(this, SeekBarActivity.class));
    }

    public void ringOnClick(View view) {
        startActivity(new Intent(this, RingColorPickerActivity.class));
    }

    public void clickOnClick(View view) {
        startActivity(new Intent(this, MulitClickActivity.class));
    }

    public void timeOnClick(View view) {
        startActivity(new Intent(this, TimeActivity.class));
    }

    public void ringProgressOnClick(View view) {
        startActivity(new Intent(this, RingProgressActivity.class));
    }

    public void wifiScanOnClick(View view) {
        startActivity(new Intent(this, WifiScanActivity.class));
    }

    public void appOnClick(View view) {
       // try {
//            PackageInfo packageInfo=getPackageManager().getPackageInfo("com.curi.medialab.poopoo", 0);
//            ComponentName comp = new ComponentName("com.curi.medialab.poopoo","com.curi.medialab.poopoo.LoginActivity");
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage("com.curi.medialab.poopoo");
//            Intent intent=new Intent();
//            intent.setComponent(comp);
            intent.setAction("android.intent.action.VIEW");
            startActivity(intent);
//        } catch (PackageManager.NameNotFoundException e) {
//            //没有安装该程序
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            File apk=copyApkFromAssets("CustomView.apk");
//            intent.setDataAndType(Uri.fromFile(apk),
//                    "application/vnd.android.package-archive");
//            MainActivity.this.startActivity(intent);
//        }
    }

    private File APK_DIR = new File(Environment.getExternalStorageDirectory().toString()+"/APK");

    private File copyApkFromAssets(String apkName){
        File f = null;
        try {
            if(!APK_DIR.exists()){
                APK_DIR.mkdirs();
            }
            InputStream is = getAssets().open(apkName);
            f = new File(APK_DIR, apkName);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len=is.read(buffer))!=-1){
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            is.close();
            return f;
        } catch (Exception e) {
            //报错不执行
        }
        return null;
    }

    public void PermissionsOnClick(View view) {
        startActivity(new Intent(this, PermissionsActivity.class));
    }
}
