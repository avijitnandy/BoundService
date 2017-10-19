package com.w3xplorers.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Created by Avijit on 10/19/2017.
 */

public class MessangerService extends Service{
    static final int MSG_SAY_HELLO = 1;
    Messenger messenger = new Messenger(new MyBinder());

    public MessangerService(){
    }

    class MyBinder extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SAY_HELLO:
                    Bundle bundle = msg.getData();
                    Toast.makeText(getApplicationContext(),bundle.getString("sms"),Toast.LENGTH_SHORT).show();
                    Message message = Message.obtain(null, MessangerService.MSG_SAY_HELLO,0,0);

                    Bundle bundle1 = new Bundle();
                    bundle1.putString("response","Your Message Delivered!!");
                    message.setData(bundle1);
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                    default:
                        super.handleMessage(msg);
                        break;
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
