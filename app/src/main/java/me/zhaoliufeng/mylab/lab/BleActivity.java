package me.zhaoliufeng.mylab.lab;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import me.zhaoliufeng.mylab.R;

/**
 * Created by zhaol on 2017/9/12.
 */

public class BleActivity extends Activity {

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private ListAdapter listAdapter;
    private BleReceiver bluetoothReceiver;
    private List<Device> bleDevList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        bluetoothReceiver = new BleReceiver();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothAdapter.enable();
        listAdapter = new ListAdapter(bleDevList);


        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(listAdapter);
    }

    public void scanOnClick(View view) {
        bluetoothAdapter.startLeScan(mBleCallBack);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.stopLeScan(mBleCallBack);
            }
        }, 3000);
    }

    public BluetoothDevice mDev;
    public BluetoothAdapter.LeScanCallback mBleCallBack = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            //过滤蓝牙中的Fulife网络
            if (device.getName() != null && device.getName().equals("Fulife")){
                //判断网络中是否已经存在该设备Mac地址
                for (Device dev : bleDevList){
                    if (dev.getMacAddress().equals(device.getAddress())){
                        if (dev.getRssi() != rssi){
                            dev.setRssi(rssi);
                        }
                        return;
                    }
                }
                Log.i("Scan Device", device.getAddress());

                //将设备添加到设备集合中
                bleDevList.add(new Device(device.getAddress(), device.getName(), rssi));
                if (mDev == null){
                    mDev = bluetoothAdapter.getRemoteDevice(bleDevList.get(0).macAddress);
                    mDev.connectGatt(BleActivity.this,  false, mBleGattCallBack);
                }

                listAdapter.notifyDataSetChanged();
            }
        }
    };

    private Timer mRssiTimer;

    private BluetoothGattCallback mBleGattCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        gatt.readRemoteRssi();
                    }
                };
                mRssiTimer = new Timer();
                mRssiTimer.schedule(task, 1000, 1000);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if (mRssiTimer != null) {
                    mRssiTimer.cancel();
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }
    };

    private class ListAdapter extends BaseAdapter {

        private List<Device> datas;

        public ListAdapter(List<Device> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(BleActivity.this).inflate(R.layout.item_lv, parent, false);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_item);
            tv.setText(datas.get(position).getString());
            return convertView;
        }
    }

    private class Device{
        private int rssi;
        private String macAddress;
        private String name;

        public Device(String macAddress, String name, int rssi) {
            this.macAddress = macAddress;
            this.name = name;
            this.rssi = rssi;
        }


        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getString(){
            return name + " : " + macAddress + " " + rssi;
        }

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }
    }

}
