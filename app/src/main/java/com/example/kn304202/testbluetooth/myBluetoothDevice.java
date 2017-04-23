package com.example.kn304202.testbluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.logging.LogRecord;

public class myBluetoothDevice extends AppCompatActivity {

    BluetoothDevice bthDevice;
    BluetoothGatt bluetoothGatt;

    List<BluetoothGattService> serviceList;
    TextView tvBthDevice;
    TextView tvCnx, tvNbServices, tvServiceUUID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bluetooth_device);

        Intent i = getIntent();
        bthDevice = i.getParcelableExtra("bthDevice");

        if(bthDevice.getName() != null)
            this.setTitle(bthDevice.getName());
        else
            this.setTitle("Unknown");

        tvNbServices = (TextView) findViewById(R.id.tvNbServices);
        tvBthDevice = (TextView) findViewById(R.id.tvBthDevice);
        tvBthDevice.setText(bthDevice.getName());
        tvCnx = (TextView) findViewById(R.id.tvCnx);
        tvServiceUUID = (TextView) findViewById(R.id.tvServiceUUID);


        final Handler mHander = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                List<BluetoothGattService> sList = (List<BluetoothGattService>) msg.obj;

                Toast.makeText(getApplicationContext(), "Services discovered !", Toast.LENGTH_SHORT).show();
                tvNbServices.setText(String.valueOf(sList.size()));
                tvServiceUUID.setText(sList.get(0).getUuid().toString());
            }
        };


        BluetoothGattCallback bthGattCallback = new BluetoothGattCallback(){

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if( newState == BluetoothProfile.STATE_CONNECTED){
                    //Toast.makeText(getApplicationContext(), "Connected !", Toast.LENGTH_SHORT).show();
                    tvCnx.setText("Connected !");
                    bluetoothGatt.discoverServices();
                } else
                    tvCnx.setText("Not connected ..");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                serviceList = bluetoothGatt.getServices();

                mHander.sendMessage(Message.obtain(mHander, 0, serviceList));

            }
        };


        bluetoothGatt = bthDevice.connectGatt(this, false, bthGattCallback);



    }


    @Override
    protected void onStop() {
        super.onStop();
        /*bluetoothGatt.disconnect();
        bluetoothGatt.close();*/
    }
}
