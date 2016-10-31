package com.example.l156435_p107855.servicos;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private Handler handler;
    private final IBinder binder = new MyBinder();


    public MyIntentService() {
        super("MyIntentService");
        handler = new Handler();

    }

    @Override
    public IBinder onBind(Intent intent){
        return binder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this){
            try {
                wait(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            String text = intent.getStringExtra("message");
            //SendMessage(text);
            SendBroadcast(text);
        }
    }

    public void SendMessage(final String text){
        Log.v("My intent service", text);

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder =  TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT );

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Título da Mensagem")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1234, notification);

    }


    private void SendBroadcast(final String text){
        Intent in = new Intent("MyIntentServiceBroadcast");
        in.putExtra("message", text);
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);


    }
    public class MyBinder extends Binder{
        MyIntentService getMyIntentService(){
            return MyIntentService.this;
        }
    }
}
