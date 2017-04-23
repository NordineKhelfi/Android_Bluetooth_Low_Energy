package com.example.kn304202.testbluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity /*implements BluetoothAdapter.LeScanCallback*/ {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private static final int REQUEST_ENABLE_BT = 0;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private SparseArray<BluetoothDevice> bthDevices;

    private ArrayList<String> bthDevicesNames;
    ArrayAdapter<String> aadapter;

    TextView tvTest;
    ListView lvBthDevices;
    int i = 0;

    Button btnScan;
    Button btnShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        bthDevices = new SparseArray<BluetoothDevice>();
        bthDevicesNames = new ArrayList<String>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

        tvTest = (TextView) findViewById(R.id.tvTest);
        lvBthDevices = (ListView) findViewById(R.id.lvBthDevices);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnShow = (Button) findViewById(R.id.btnShow);




        lvBthDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, myBluetoothDevice.class);
                intent.putExtra("bthDevice", bthDevices.get(position));

                startActivity(intent);
            }
        });

    }

    public void onScanBtnClick(View v){
        Toast.makeText(this, "Scan started", Toast.LENGTH_SHORT).show();
        //mBluetoothAdapter.startLeScan(this);
        /*bthDevices.clear();
        bthDevicesNames.clear();*/
        mBluetoothLeScanner.startScan(mScanCallback);
    }

    /*public void onShowBtnClick(View v){
        ArrayAdapter<String> aadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, bthDevicesNames);
        lvBthDevices.setAdapter(aadapter);
    }*/

    /*@Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Toast.makeText(this, "Someone detected !!!", Toast.LENGTH_LONG).show();
    }*/



    /*@Override
    protected void onResume() {
        super.onResume();

    }*/

    @Override
    protected void onPause() {
        super.onPause();
        //mBluetoothAdapter.stopLeScan(this);
        if( mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null)
            mBluetoothLeScanner.stopScan(mScanCallback);
        Toast.makeText(this, "Scan stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mBluetoothAdapter.stopLeScan(this);
        if( mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null)
        mBluetoothLeScanner.stopScan(mScanCallback);
        //Toast.makeText(this, "Scan stopped", Toast.LENGTH_SHORT).show();
    }


    ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //Toast.makeText(getApplicationContext(), "Someone detected !!!", Toast.LENGTH_LONG).show();

            if( !bthDevicesNames.contains(result.getDevice().getName()) && bthDevices.indexOfValue(result.getDevice()) < 0){

                bthDevices.put(i, result.getDevice());
                i++;
                if(result.getDevice().getName() != null)
                    bthDevicesNames.add(result.getDevice().getName());
                else
                    bthDevicesNames.add("Unknown..");

                if(aadapter == null){
                    aadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, bthDevicesNames);
                    lvBthDevices.setAdapter(aadapter);
                }

                aadapter.notifyDataSetChanged();
            }



            tvTest.setText(result.getDevice().getName());


            /*btnScan.setVisibility(Button.GONE);
            btnShow.setVisibility(Button.VISIBLE);*/

        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(getApplicationContext(), "Fail ..", Toast.LENGTH_SHORT).show();
        }
    };


}
