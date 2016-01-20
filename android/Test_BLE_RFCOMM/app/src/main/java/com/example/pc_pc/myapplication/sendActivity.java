package com.example.pc_pc.myapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;



/**
 * Created by PC-PC on 2016-01-06.+
 */
public class sendActivity extends AppCompatActivity {
    ConnectThread connectThread;
    ConnectedThread connectedThread;
    ConnectThread whileThread;
    ConnectedThread mwhiledThread;
    long timeToConnectStart;
    long timeToConnectEnd;
    Handler mHandler = new MyHandler();
    private EditText edittext = null;
    private Vibrator vibe;
    View sendButton;
    View endButton;
    ScrollView scrollView;
    int show = 0;
    String toSend;
    ArrayAdapter<String> mArrayAdapter2;
    ListView listview;
    byte[] recvM;
    View testButton;
    View whileButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        BluetoothDevice device = getIntent().getParcelableExtra("device");
        connectThread = new ConnectThread(device);
        connectThread.start();
       // Log.d("Debug", "onCreate: ");
        while( show != 1) {
        }
        sendButton = findViewById(R.id.sendButton);
        edittext = (EditText) findViewById(R.id.sendString);
        endButton = findViewById(R.id.endButton);
        testButton = findViewById(R.id.testButton);
        whileButton = findViewById(R.id.whileButton);

        sendButton.setVisibility(View.VISIBLE);
        edittext.setVisibility(View.VISIBLE);
        endButton.setVisibility(View.VISIBLE);
        testButton.setVisibility(View.VISIBLE);
        whileButton.setVisibility(View.VISIBLE);

        toSend = null;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectedThread.write( edittext.getText().toString().getBytes() );
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 4; i++) {
                    testSend((int) Math.pow(2, i) * 100);
                }
                vibe.vibrate(500);
            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSend = "end";
                connectedThread.write(toSend.getBytes());
                System.exit(1);
            }
        });
        whileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tmp = Double.valueOf(edittext.getText().toString())*1000*3;
                whileSend( (long)tmp );
                vibe.vibrate(500);
            }
        });
    }

    public void whileSend(long num) {
        toSend = "CheckStart:20";
        recvM = new byte[ 20 ];
        Log.d("Time", String.valueOf(num));
        connectedThread.write(toSend.getBytes());
        int i=0;
        while( i <num){
            connectedThread.read(recvM);
            connectedThread.write(recvM);
            i++;
        }
        connectedThread.read(recvM);
        connectedThread.write(new String("CheckEnd").getBytes());
        connectedThread.read(recvM);
    }

    public void testSend(int num) {
        toSend = "CheckStart:" + String.valueOf(num);
        recvM = new byte[ num ];
        Log.d("NUM", String.valueOf(num));
        connectedThread.write(toSend.getBytes());
        for (int i = 0; i < 999; i++) {
            connectedThread.read(recvM);
            connectedThread.write(recvM);
        }
        connectedThread.read(recvM);
        connectedThread.write(new String("CheckEnd").getBytes());
        connectedThread.read(recvM);
    }
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                timeToConnectStart = System.currentTimeMillis();
                Log.d("TAG", "Connect start ");
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {e.printStackTrace();}
            mmSocket = tmp;
        }

        public void run() {

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                timeToConnectEnd = System.currentTimeMillis();
                Log.d("TAG", "Connect end ");
                Log.d("TAG", String.valueOf(timeToConnectEnd - timeToConnectStart));
                show =1;
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.e("TAG", "enable to connect :::: 0 ");
                connectException.printStackTrace();
                try {
                    mmSocket.close();
                } catch (IOException closeException) {closeException.printStackTrace();}
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {e.printStackTrace();}
        }

        private void manageConnectedSocket(BluetoothSocket socket){
            connectedThread = new ConnectedThread(socket);
            connectedThread.start();
        }

    }
    private static class MyHandler extends Handler {

        final int MESSAGE_READ = 9999;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    Log.d("TEST_MEESAGE_READ", msg.obj.toString());
                    break;
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        final int MESSAGE_READ = 9999;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {e.printStackTrace();}

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
             // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                while(show != 1) {
                    try {
                        connectedThread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                try {
//                    // Read from the InputStream
//                    bytes=mmInStream.read();
//                    // Send the obtained bytes to the UI activity
//                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
//                } catch (IOException e) {
//                    break;
//                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {e.printStackTrace();}
        }

        public void read(byte[] buffer) {
            try {
                mmInStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {e.printStackTrace();}
        }

    }
}
