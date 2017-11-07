package me.zhaoliufeng.mylab.lab;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        lv.setOnItemClickListener(onItemClickListener);
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
    public List<BluetoothGattCharacteristic> gattCharacteristics;
    public BluetoothGatt bleGatt;
    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //拿到第一个设备 并连接
            mDev = bluetoothAdapter.getRemoteDevice(bleDevList.get(position).macAddress);
            Log.i("Scan Device", "Connect -- " + mDev.toString());
            mDev.connectGatt(BleActivity.this, false, mBleGattCallBack);
        }
    };

    public BluetoothAdapter.LeScanCallback mBleCallBack = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            //过滤蓝牙中的Fulife网络
            if (device.getName() != null && device.getName().equals("Fulife")) {
                boolean devExist = false;
                int devIndex = -1;
                //判断网络中是否已经存在该设备Mac地址
                for (Device dev : bleDevList) {
                    devIndex++;
                    if (dev.getMacAddress().equals(device.getAddress())) {
                        devExist = true;
                        if (dev.getRssi() != rssi) {
                            dev.setRssi(rssi);
                            break;
                        } else {
                            //如果已经在列表中存在 且强度值不变就不在往列表中添加
                            return;
                        }
                    }
                }
                Log.i("Scan Device", device.getAddress());

                //将设备添加到设备集合中
                if (devExist) {
                    bleDevList.remove(devIndex);
                    bleDevList.add(devIndex, new Device(device.getAddress(), device.getName(), rssi));
                } else {
                    bleDevList.add(new Device(device.getAddress(), device.getName(), rssi));
                }

                listAdapter.notifyDataSetChanged();
            }
        }
    };

    private BluetoothGattCallback mBleGattCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i("Scan Device", "onServicesDiscovered");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //在这里可以对服务进行解析，寻找到你需要的服务
                if (gatt.getServices() == null)
                    return;
                for (BluetoothGattService gattService : gatt.getServices()) {
                    // 遍历出gattServices里面的所有服务
                    gattCharacteristics = gattService.getCharacteristics();
                    bleGatt = gatt;
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i("Scan Device", "onCharacteristicRead");

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i("Scan Device", "onCharacteristicWrite");
        }
    };

    public void offOnClick(View view) {
        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            // 遍历每条服务里的所有Characteristic Command 命令UUid
            if (gattCharacteristic.getUuid().toString().equalsIgnoreCase("00010203-0405-0607-0809-0a0b0c0d1912")) {
                Log.i("Scan Device", "All OFF");
                gattCharacteristic.setValue(new byte[]{0x11, 0x11, 0x12, 0x00, 0x00, (byte) 0xff, (byte) 0xff
                        , (byte) 0xD0, 0x11, 0x02, 0x00, 0x00, 0x00});
                if (bleGatt.writeCharacteristic(gattCharacteristic)){
                    Log.i("Scan Device", "Success");
                }else {
                    Log.i("Scan Device", "Fail");
                }
            }
        }
    }

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

    /**
     * 设备实体类
     */
    private class Device {
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

        public String getString() {
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
