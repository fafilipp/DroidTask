package de.htwg.android.taskmanager.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ArduinoConnector {

	// private final static int REQUEST_ENABLE_BT = 133;

	@SuppressLint("NewApi")
	public void showDevices(Activity activity) throws IOException {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			Log.e("bluetooth null", "device doesn't support bluetooth.");
			// TODO: device doesn't support bluetooth.
		}
		if (!bluetoothAdapter.isEnabled()) {
			Log.e("bluetooth disabled", "device hasn't bluetooth enabled.");
			// TODO: device hasn't bluetooth enabled.
			// Intent enableBtIntent = new
			// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				Log.i("Paired device found", device.getName() + "\n" + device.getAddress());
				// if (device.getName().equals("FFI-NOTEBOOK")) {
				// ParcelUuid[] uuids = device.getUuids();
				// for (ParcelUuid uuid : uuids) {
				// Log.i("Connecting (UUID)", uuid.getUuid().toString());
				// }
				// Log.i("Connecting", "Connecting to FFI-Notebook");
				// BluetoothSocket socket = connectToDevice(device);
				// Log.i("Connecting", "Connecting to FFI-Notebook, success");
				// OutputStream os = socket.getOutputStream();
				// InputStream is = socket.getInputStream();
				// byte[] data = "Hello, my name is fabio".getBytes();
				// os.write(data);
				// Log.i("Connecting", "Data written");
				// }
			}
		}

		BroadcastReceiver receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.i("Discovered device found", device.getName() + "\n" + device.getAddress());
				}
			}
		};

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		activity.registerReceiver(receiver, filter);
		bluetoothAdapter.startDiscovery();
	}

	public BluetoothSocket connectToDevice(BluetoothDevice device) throws IOException {
		BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
		socket.connect();
		return socket;
	}

	public float getTemperature(int measureTimes, OutputStream os, InputStream is) throws IOException {
		String measureTimesAsString = String.valueOf(measureTimes);
		byte[] outputData = measureTimesAsString.getBytes();
		os.write(outputData);

		int len = -1;
		byte[] buffer = new byte[2048];
		StringBuilder temperatureAsString = new StringBuilder();
		while ((len = is.read(buffer)) >= 0) {
			temperatureAsString.append(new String(buffer, 0, len));
		}
		return Float.valueOf(temperatureAsString.toString());
	}
}