package de.htwg.android.taskmanager.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import de.htwg.android.taskmanager.activity.MainActivity;
import de.htwg.android.taskmanager.activity.R;

public class ArduinoTemperatureMeasurement {

	private MainActivity activity;

	public ArduinoTemperatureMeasurement(MainActivity activity) {
		this.activity = activity;
	}

	/**
	 * Checks if the Bluetooth device is not available or not activated.
	 * 
	 * @return true if the Bluetooth device is not available and not activated.
	 */
	public boolean isBluetoothDisabled() {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return (bluetoothAdapter == null || !bluetoothAdapter.isEnabled());
	}

	public void registerBluetoothStateChangeListener() {
		final BroadcastReceiver stateReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				switch (state) {
				case BluetoothAdapter.STATE_TURNING_OFF:
				case BluetoothAdapter.STATE_TURNING_ON:
					break;
				case BluetoothAdapter.STATE_OFF:
				case BluetoothAdapter.STATE_ON:
					activity.invalidateOptionsMenu();
					break;
				}
			}
		};
		IntentFilter stateFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		activity.registerReceiver(stateReceiver, stateFilter);
	}

	/**
	 * Starts the Bluetooth procedure. It will follow following sequence: search
	 * devices --> show devices --> (user) select arduino --> connect to arduino
	 * --> input measure times --> send measure times to arduino --> wait for
	 * temperature response.
	 */
	public void startBluetoothProcess() {
		// If Bluetooth is unavailable show a toast message.
		if (isBluetoothDisabled()) {
			Toast.makeText(activity, "Bluetooth is disabled on device, please activate.", Toast.LENGTH_LONG).show();
		} else {
			// shows up an progress dialog as long as the device is discovering
			// for bluetooth devices
			final ProgressDialog progressDialog = ProgressDialog.show(activity, "Please wait", "Searching Bluetooth devices", true);

			final List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			// Get paired devices
			Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					devices.add(device);
					Log.i("Bluetooth device found (paired)", device.getName() + " - " + device.getAddress());
				}
			}

			// Discover visible devices
			final BroadcastReceiver discoverReceiver = new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					devices.add(device);
					Log.i("Bluetooth device found (discovered)", device.getName() + " - " + device.getAddress());
				}
			};
			IntentFilter discoverFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			activity.registerReceiver(discoverReceiver, discoverFilter);

			// React on discover finished event
			final BroadcastReceiver actionFinishedReceiver = new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
					// unregister the receivers
					activity.unregisterReceiver(discoverReceiver);
					activity.unregisterReceiver(this);

					// dismiss the progress dialog
					progressDialog.dismiss();

					// create the selection dialog
					createBluetoothDeviceSelectDialog(devices);
				}
			};
			IntentFilter actionFinishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			activity.registerReceiver(actionFinishedReceiver, actionFinishedFilter);

			// start discovering
			bluetoothAdapter.startDiscovery();
		}
	}

	/**
	 * Creates an selection dialog for selecting the BluetoothDevice, which
	 * provides the temperature (Arduino)
	 * 
	 * @param devices
	 *            the list of available devices.
	 */
	private void createBluetoothDeviceSelectDialog(final List<BluetoothDevice> devices) {
		String[] deviceArray = new String[devices.size()];
		for (int i = 0; i < deviceArray.length; i++) {
			deviceArray[i] = devices.get(i).getName();
		}
		AlertDialog.Builder bluetoothDialog = new AlertDialog.Builder(activity);
		bluetoothDialog.setTitle("Select Device");
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.bluetooth_discovery, null);
		final ListView listView = (ListView) view.findViewById(R.id.deviceList);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, deviceArray);
		listView.setAdapter(arrayAdapter);
		bluetoothDialog.setView(view);

		final AlertDialog alertDialog = bluetoothDialog.create();
		alertDialog.show();

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// connect to the selected device
				BluetoothDevice bluetoothDevice = devices.get(arg2);
				connectToBluetoothDevice(bluetoothDevice);
				alertDialog.dismiss();
			}
		});
	}

	/**
	 * Connect to the selected BluetoothDevice from the ListView.
	 * 
	 * @param bluetoothDevice
	 *            the selected BluetoothDevice.
	 */
	private void connectToBluetoothDevice(BluetoothDevice bluetoothDevice) {
		BluetoothSocket socket = null;
		try {
			socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();
			// if no exception thrown, connection is established
			Toast.makeText(activity, String.format("Connected to device %s.", bluetoothDevice.getName()), Toast.LENGTH_LONG).show();
			createMeasurementInputDialog(socket);
			// OutputStream os = socket.getOutputStream();
			// os.write("Das ist ein Test".getBytes());
		} catch (IOException e) {
			Toast.makeText(activity, String.format("Can't establish connection to device %s.", bluetoothDevice.getName()),
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Creates an selection dialog for selecting the BluetoothDevice, which
	 * provides the temperature (Arduino)
	 * 
	 * @param devices
	 *            the list of available devices.
	 */
	private void createMeasurementInputDialog(final BluetoothSocket socket) {
		AlertDialog.Builder bluetoothDialog = new AlertDialog.Builder(activity);
		bluetoothDialog.setTitle("Number of temperature measurements");
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.measurements_dialog, null);
		final EditText etNumber = (EditText) view.findViewById(R.id.et_number);
		etNumber.setText("5");
		bluetoothDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String text = etNumber.getText().toString();
				try {
					OutputStream os = socket.getOutputStream();
					final InputStream is = socket.getInputStream();
					os.write(text.getBytes());
					new Thread(new Runnable() {
						public void run() {
							try {
								final byte[] bytes = new byte[2048];
								StringBuilder b = new StringBuilder();
								while (is.read(bytes) > -1) {
									char currentChar = 0;
									for (int i = 0; i < bytes.length; i++) {
										if (bytes[i] == 0) {
											Log.i("InputStream (i)", String.valueOf(i));
											break;
										}
										currentChar = (char) bytes[i];
										if (currentChar != 's') {
											b.append(currentChar);
										}
										Log.i("InputStream (char)", String.valueOf(currentChar));
									}
									if (currentChar == 's') {
										break;
									}
								}
								final String value = b.toString();
								Log.i("InputStream (read temperature)", b.toString());
								final float temperature = Float.parseFloat(value);
								activity.runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(activity, String.format("The temperature is %s", value), Toast.LENGTH_LONG).show();
										if(temperature < 30.00) {
											activity.createArduinoTask();
										}
									}
								});
							} catch (IOException e) {
								Log.e("IOException", e.getMessage(), e);
							}
						}
					}).start();
				} catch (IOException e) {
					Log.e("IOException", e.getMessage(), e);
				}
				dialog.dismiss();
			}
		});
		bluetoothDialog.setView(view);
		bluetoothDialog.create();
		bluetoothDialog.show();
	}

}
