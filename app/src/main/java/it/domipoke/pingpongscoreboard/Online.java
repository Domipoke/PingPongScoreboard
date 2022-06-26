package it.domipoke.pingpongscoreboard;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Online extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_ENABLE_BT = 6;
    // 1-3 vs 2-4
    Button pl1;
    Button pl2;
    Button pl3;
    Button pl4;

    ArrayList<Map<String, Object>> Users = new ArrayList<>(4);

    //BL
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        /*
        binding = ActivityOnlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        */


        findViewById(R.id.ble).setOnClickListener(v -> {
            // Ask for permission and to active nfc. On phone contact transfer data to each other
            //canNFC();
            setupBLE();
            scan();
            //Open a Dialog where app ask if user want to HOST or JOIN
            // - Only one can Host
            // - Max 3 player can join
            // - About everyone can spectate
            // openDialogJoinHost();
            // Search other users in proximity
            // searchUsers();
            // Click and open dialog in order to change player team
            // setupPLButton();
            //
        });
    }

    private void setupBLE() {
        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                setResult(REQUEST_ENABLE_BT, enableBtIntent);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    Utils.FastToast(this, "Permessi non concessi", 0);
                }
                startActivity(enableBtIntent);
            }
        }
    }

    private void scan() {

    }


}
