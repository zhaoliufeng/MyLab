package me.zhaoliufeng.mylab.lab;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import me.zhaoliufeng.customviews.ScanView;
import me.zhaoliufeng.mylab.R;

/**
 * Created by We-Smart on 2017/7/26.
 */

public class ShaderActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shader);
    }

    public void scanOnClick(View view) {
        ScanView scanView = (ScanView) findViewById(R.id.scan_view);
        if (scanView.getScanStatus()){
            scanView.startScan();
        }else{
            scanView.stopScan();
        }
    }
}
