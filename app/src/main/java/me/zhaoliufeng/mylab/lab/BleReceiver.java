package me.zhaoliufeng.mylab.lab;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by zhaol on 2017/9/13.
 */

public class BleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //得到intent里面的信息
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Toast.makeText(context, "发现蓝牙设备", Toast.LENGTH_SHORT).show();
            //System.out.println(device.getAddress());
            //list.add(device.getName() + "\n" + device.getAddress());
            //PairedMaclist.add(device.getAddress());
            //listAdapter.notifyDataSetChanged();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Toast.makeText(context, "搜索完成", Toast.LENGTH_SHORT).show();
        }
    }
}
