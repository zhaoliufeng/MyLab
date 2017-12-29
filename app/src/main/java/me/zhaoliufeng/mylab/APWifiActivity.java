package me.zhaoliufeng.mylab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.ArrayList;

import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

public class APWifiActivity extends AppCompatActivity {

    private WifiManager mWifiManager;

    private EditText mEdtApName, mEdtApPasword;
    private TextView mTvState;
    private ServerSocket serverSocket;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            while (true){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apwifi);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mEdtApName = (EditText) findViewById(R.id.edt_ap_name);
        mEdtApPasword = (EditText) findViewById(R.id.edt_ap_password);
        mTvState = (TextView) findViewById(R.id.tv_state);

        try {
            serverSocket = new ServerSocket(30000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        registerReceiver(wifiReceiver, new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED"));
    }

    /**
     * 创建热点
     *
     * @param mSSID   热点名称
     * @param mPasswd 热点密码
     * @param isOpen  是否是开放热点
     */
    public void startWifiAp(String mSSID, String mPasswd, boolean isOpen) {
        Method method1 = null;
        try {
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            if (isOpen) {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            }
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            method1.invoke(mWifiManager, netConfig, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热点名
     **/
    public String getApSSID() {
        try {
            Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
            if (localMethod == null) return null;
            Object localObject1 = localMethod.invoke(this.mWifiManager, new Object[0]);
            if (localObject1 == null) return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null) return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
            if (localField1 == null) return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null) return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null) return null;
            localField2.setAccessible(false);
            String str = (String) localObject3;
            return str;
        } catch (Exception localException) {
        }
        return null;
    }

    /**
     * 检查是否开启Wifi热点
     */
    public boolean isWifiApEnabled() {
        try {
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭热点
     */
    public void closeWifiAp() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (isWifiApEnabled()) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //打开热点
    public void onOpenApClick(View view) {
        String apName = mEdtApName.getText().toString();
        String apPassword = mEdtApPasword.getText().toString();
        int status = mWifiManager.getWifiState();
        //如果wifi是打开的 需要先关闭wifi
        if (status == WIFI_STATE_ENABLED){
            mWifiManager.setWifiEnabled(false);
        }
        startWifiAp(apName, apPassword, true);
    }

    //关闭热点
    public void onCloseApClick(View view) {
        closeWifiAp();
    }

    //检查当前热点状态
    public void onCheckStateClick(View view){
        ArrayList<String> ips = getConnectedIP();
        DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
        //如果有ip连接信息 就使用socket连接
        if (ips != null && ips.size() > 0){
            mHandler.sendEmptyMessageDelayed(0,0);
        }
        if (isWifiApEnabled()) {
            if (ips != null && ips.size() > 0){
                mTvState.setText("ConnectIp : " + ips.get(0));
            }else {
                mTvState.setText(getApSSID() + " AP Enabled" + dhcpInfo.serverAddress);
            }
        } else {
            mTvState.setText("AP Disabled");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                //便携式热点的状态为：10---正在关闭；11---已关闭；12---正在开启；13---已开启
                int state = intent.getIntExtra("wifi_state", 0);
                showApState(state);
            }
        }
    };

    private void showApState(int state) {
        switch (state) {
            case 11:
                mTvState.setText("AP Disabled");
                break;
            case 13:
                mTvState.setText(getApSSID() + "AP Enabled");
                break;
        }
    }

    private ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIP = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
            connectedIP.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }
}
