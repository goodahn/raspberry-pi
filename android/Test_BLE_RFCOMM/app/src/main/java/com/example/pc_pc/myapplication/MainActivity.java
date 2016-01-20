package com.example.pc_pc.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;

    static final int REQUEST_ENABLE_BT = 1;

    ListView listView;
    ArrayAdapter<String> mArrayAdapter;
    ArrayList<BluetoothDevice> devices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        mArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());

        listView.setAdapter(mArrayAdapter);

        devices = new ArrayList<>();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if( mBluetoothAdapter == null ) {
            Toast.makeText(this, "This device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if( !mBluetoothAdapter.isEnabled() ) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                getPairedDevices();
            }
        }

        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(mmReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.finish();
                BluetoothDevice selectedDevice = devices.get(position);
                Intent intent = new Intent(MainActivity.this, sendActivity.class);
                intent.putExtra("device", selectedDevice);
                mBluetoothAdapter.cancelDiscovery();
                startActivity(intent);
//                Intent intent = new Intent(MainActivity.this, MouseActivity.class);
//                intent.putExtra("device", selectedDevice);
//                mBluetoothAdapter.cancelDiscovery();
//                startActivity(intent);
            }
        });
    }

    private void selectDevice() {

    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if( pairedDevices.size() > 0) {
            for( BluetoothDevice device : pairedDevices ) {
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                devices.add(device);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) {
                    // 블루투스가 활성 상태로 변경됨
                    Toast.makeText(this, "Bluetooth is activated!", Toast.LENGTH_SHORT).show();
                    getPairedDevices();
                } else if(resultCode == RESULT_CANCELED) {
                    // 블루투스가 비활성 상태임
                    finish();  //  어플리케이션 종료
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF){
                    // Bluetooth is disconnected, do handling here
                    Toast.makeText(MainActivity.this, "bluetooth is turned off", Toast.LENGTH_SHORT).show();
                } else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON){
                    Toast.makeText(MainActivity.this, "bluetooth is turned on", Toast.LENGTH_SHORT).show();
                    getPairedDevices();
                }
            }
        }
    };

    private final BroadcastReceiver mmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

}
