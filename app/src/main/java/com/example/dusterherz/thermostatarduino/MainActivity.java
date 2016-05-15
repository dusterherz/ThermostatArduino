package com.example.dusterherz.thermostatarduino;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private int actualTemperature;
    private int setTemperature;
    private TextView actualTemperatureText;
    private TextView setTemperatureText;
    private SeekBar seekBar;
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actualTemperature = 0;
        setTemperature = 0;
        actualTemperatureText = (TextView) findViewById(R.id.textViewTemperatureValue);
        setTemperatureText = (TextView) findViewById(R.id.textViewTemperatureValueSet);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        actualTemperatureText.setText(getResources().getString(R.string.temperature, actualTemperature));
        setTemperatureText.setText(getResources().getString(R.string.temperature, setTemperature));

        if (mBluetoothAdapter == null) {
            DialogFragment newFragment = new NoBluetoothDialogFragment();
            newFragment.show(getFragmentManager(), "No Bluetooth");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                setTemperature = value - 50;
                setTemperatureText.setText(getResources().getString(R.string.temperature, setTemperature));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO send informations to the arduino
            }
        });
    }

    private void setupConnexion() {
        //// TODO: 18/04/2016 create a connexion with the arduino. 
    }

    static public class NoBluetoothDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.no_bluetooth)
                    .setNeutralButton(R.string.fermer, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (getActivity() != null)
                                getActivity().finish();
                        }
                    });
            return builder.create();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupConnexion();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(getApplicationContext(), R.string.bt_not_enabled,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}
