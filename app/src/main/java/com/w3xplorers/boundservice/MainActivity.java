package com.w3xplorers.boundservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Messenger messenger;

    static final int MSG_SAY_HELLO = 1;

    EditText mySMS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySMS = (EditText) findViewById(R.id.mySMS);

        bindService(new Intent(this, MessangerService.class), serviceConnection, BIND_AUTO_CREATE);


        ((Button) findViewById(R.id.message)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = Message.obtain(null, MessangerService.MSG_SAY_HELLO, 0, 0);

                message.replyTo = clientMessenger;

                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("sms", mySMS.getText().toString());
                    message.setData(bundle);
                    messenger.send(message);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private Messenger clientMessenger = new Messenger(new ClientBinder());

    class ClientBinder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Bundle bundle = msg.getData();
                    Toast.makeText(getApplicationContext(), bundle.getString("response"), Toast.LENGTH_LONG).show();

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("BindMessangerActivity: ", "--onServiceDisconnected--");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MessangerService.class));

    }
}